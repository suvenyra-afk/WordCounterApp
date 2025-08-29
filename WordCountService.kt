package lt.wordcounter.app.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.*
import lt.wordcounter.app.MainActivity
import lt.wordcounter.app.R
import lt.wordcounter.app.audio.SimpleVad
import lt.wordcounter.app.audio.SpeakerVerifier
import lt.wordcounter.app.data.Repository
import lt.wordcounter.app.storage.Prefs
import java.util.concurrent.atomic.AtomicBoolean
import be.tarsos.dsp.AudioEvent
import be.tarsos.dsp.mfcc.MFCC

class WordCountService : Service() {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private lateinit var repo: Repository
    private lateinit var prefs: Prefs

    private val running = AtomicBoolean(false)

    override fun onCreate() {
        super.onCreate()
        repo = Repository(this)
        prefs = Prefs(this)
        createNotificationChannel()
        startForeground(1, buildNotification(0))
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (running.compareAndSet(false, true)) {
            scope.launch { runLoop() }
        }
        return START_STICKY
    }

    override fun onDestroy() {
        running.set(false)
        scope.cancel()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun buildNotification(today: Int): Notification {
        val openIntent = Intent(this, MainActivity::class.java)
        val pi = PendingIntent.getActivity(this, 0, openIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        return NotificationCompat.Builder(this, "word_channel")
            .setSmallIcon(R.drawable.ic_mic)
            .setContentTitle("Žodžių skaičiavimas")
            .setContentText("Šiandien: $today žodž.")
            .setContentIntent(pi)
            .setOngoing(true)
            .build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= 26) {
            val ch = NotificationChannel("word_channel",
                "Word counting",
                NotificationManager.IMPORTANCE_LOW)
            ch.description = "Counts your spoken words in background"
            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
                .createNotificationChannel(ch)
        }
    }

    private suspend fun runLoop() = withContext(Dispatchers.Default) {
        val sr = 16000
        val chan = AudioFormat.CHANNEL_IN_MONO
        val fmt = AudioFormat.ENCODING_PCM_16BIT
        val minBuf = AudioRecord.getMinBufferSize(sr, chan, fmt)
        val rec = AudioRecord(MediaRecorder.AudioSource.VOICE_RECOGNITION, sr, chan, fmt, minBuf*2)
        val vad = SimpleVad(sr)
        val mfcc = MFCC(1024, sr.toFloat(), 13, 40, 300f, 8000f)
        val useFilter = prefs.enableSpeakerFilter
        val targetPrint = prefs.voiceprint
        val simThreshold = 0.82f

        val buf = ShortArray(1024)
        val rmsInit = mutableListOf<Double>()

        var speaking = false
        var speechStart = 0L
        var speechFrames = 0
        var silenceFrames = 0
        var mfccFrames = mutableListOf<FloatArray>()
        var todayShown = 0

        rec.startRecording()

        // Calibrate noise floor
        repeat(50) {
            val n = rec.read(buf, 0, buf.size)
            if (n > 0) rmsInit += vad.rms(buf, n)
            delay(20)
        }
        vad.calibrateNoise(rmsInit)

        while (running.get()) {
            val n = rec.read(buf, 0, buf.size)
            if (n <= 0) {
                delay(10)
                continue
            }
            val rms = vad.rms(buf, n)
            val isSpeech = vad.isSpeech(rms)

            if (isSpeech) {
                if (!speaking) {
                    speaking = true
                    speechStart = System.currentTimeMillis()
                    speechFrames = 0
                    silenceFrames = 0
                    mfccFrames = mutableListOf()
                }
                speechFrames++

                // collect MFCC if speaker filter enabled
                if (useFilter) {
                    val floatBuf = FloatArray(n) { i -> buf[i].toFloat() }
                    val ae = AudioEvent(null, 0, 0, sr.toFloat())
                    // Simple framing for MFCC: skip actual windowing for brevity
                    mfcc.process(floatBuf)
                    val coeffs = FloatArray(13)
                    for (i in 0 until 13) coeffs[i] = mfcc.mfcc[i]
                    mfccFrames.add(coeffs)
                }
            } else if (speaking) {
                silenceFrames++
                // end of segment after ~250ms of silence
                if (silenceFrames > 12) {
                    speaking = false
                    val now = System.currentTimeMillis()
                    val durMs = now - speechStart
                    if (durMs > 300) {
                        var accept = true
                        if (useFilter && targetPrint != null && mfccFrames.isNotEmpty()) {
                            val vp = SpeakerVerifier.voiceprint(mfccFrames)
                            val sim = SpeakerVerifier.cosineSim(vp, targetPrint)
                            accept = sim >= simThreshold
                        }
                        if (accept) {
                            val wps = prefs.wordsPerSecond.coerceIn(0.5f, 5.0f)
                            val words = ((durMs / 1000.0) * wps).toInt().coerceAtLeast(1)
                            repo.addWords(now, words)
                            // update notification
                            val today = repo.getTodayTotal()
                            if (today != todayShown) {
                                startForeground(1, buildNotification(today))
                                todayShown = today
                            }
                            // cleanup older than 30 days
                            val monthAgo = now - 30L*24*60*60*1000
                            repo.cleanup(monthAgo)
                        }
                    }
                    mfccFrames.clear()
                }
            }
            delay(20)
        }

        rec.stop()
        rec.release()
    }
}
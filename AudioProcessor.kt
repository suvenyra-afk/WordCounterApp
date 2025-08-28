package lt.wordcounter.app.audio

import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import be.tarsos.dsp.AudioDispatcher
import be.tarsos.dsp.AudioEvent
import be.tarsos.dsp.io.android.AudioDispatcherFactory
import be.tarsos.dsp.mfcc.MFCC
import kotlin.math.*

class SimpleVad(private val sampleRate: Int = 16000) {
    private var noiseRms = 0.0
    private var calibrated = false

    fun calibrateNoise(rmsList: List<Double>) {
        // Median RMS as noise floor
        val sorted = rmsList.sorted()
        noiseRms = sorted[sorted.size / 2]
        calibrated = true
    }

    fun isSpeech(rms: Double): Boolean {
        val thr = max(0.01, noiseRms * 2.5)
        return rms > thr
    }

    fun rms(buf: ShortArray, len: Int): Double {
        var sum = 0.0
        for (i in 0 until len) { val v = buf[i].toDouble(); sum += v*v }
        return sqrt(sum / max(1, len))
    }
}

object SpeakerVerifier {
    fun cosineSim(a: FloatArray, b: FloatArray): Float {
        var dot = 0.0
        var na = 0.0
        var nb = 0.0
        for (i in a.indices) {
            dot += a[i]*b[i]
            na += a[i]*a[i]
            nb += b[i]*b[i]
        }
        return (dot / (sqrt(na)*sqrt(nb) + 1e-9)).toFloat()
    }

    fun voiceprint(frames: List<FloatArray>): FloatArray {
        // Average MFCC vector
        val n = frames.size
        val dim = frames.first().size
        val out = FloatArray(dim)
        for (f in frames) for (i in 0 until dim) out[i]+=f[i]
        for (i in 0 until dim) out[i] /= max(1, n)
        return out
    }
}
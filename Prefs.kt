package lt.wordcounter.app.storage

import android.content.Context
import org.json.JSONArray

class Prefs(context: Context) {
    private val sp = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)

    var wordsPerSecond: Float
        get() = sp.getFloat("wps", 2.5f)
        set(value) { sp.edit().putFloat("wps", value).apply() }

    var enableSpeakerFilter: Boolean
        get() = sp.getBoolean("spk_filter", false)
        set(v) { sp.edit().putBoolean("spk_filter", v).apply() }

    var voiceprint: FloatArray?
        get() {
            val s = sp.getString("voiceprint", null) ?: return null
            val arr = JSONArray(s)
            return FloatArray(arr.length()) { i -> arr.getDouble(i).toFloat() }
        }
        set(v) {
            if (v == null) sp.edit().remove("voiceprint").apply()
            else {
                val arr = JSONArray()
                v.forEach { arr.put(it.toDouble()) }
                sp.edit().putString("voiceprint", arr.toString()).apply()
            }
        }
}
package com.suvenyra.wordcounterapp

import android.content.Context
import android.content.SharedPreferences

class PremiumManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("premium_prefs", Context.MODE_PRIVATE)

    fun isPremiumActive(): Boolean {
        return prefs.getBoolean("is_premium", false)
    }

    fun activatePremium() {
        prefs.edit().putBoolean("is_premium", true).apply()
    }

    fun redeemCode(code: String): Boolean {
        // Pvz. slaptas kodas: VIP2025
        return if (code == "VIP2025") {
            activatePremium()
            true
        } else {
            false
        }
    }
}

package com.suvenyra.wordcounterapp

class PremiumManager {

    private val validCodes = listOf(
        "VIP2024", // tavo testinis kodas
        "FAMILY123",
        "FRIENDPASS"
    )

    private var isPremium: Boolean = false

    fun redeemCode(code: String): Boolean {
        return if (validCodes.contains(code)) {
            isPremium = true
            true
        } else {
            false
        }
    }

    fun hasPremium(): Boolean {
        return isPremium
    }
}

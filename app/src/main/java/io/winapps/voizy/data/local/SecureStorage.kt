package io.winapps.voizy.data.local

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class SecureStorage(context: Context) {
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val prefs = EncryptedSharedPreferences.create(
        context,
        "secure_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveUserId(userId: Long) {
        prefs.edit()
            .putLong("userId", userId)
            .apply()
    }

    fun saveApiKey(apiKey: String) {
        prefs.edit()
            .putString("apiKey", apiKey)
            .apply()
    }

    fun saveToken(token: String) {
        prefs.edit()
            .putString("token", token)
            .apply()
    }

    fun getUserId(): Long? {
        val stored = prefs.getLong("userId", -1L)
        return if (stored == -1L) null else stored
    }

    fun getApiKey(): String? {
        return prefs.getString("apiKey", null)
    }

    fun getToken(): String? {
        return prefs.getString("token", null)
    }

    fun logout() {
        prefs.edit().remove("userId").apply()
        prefs.edit().remove("apiKey").apply()
        prefs.edit().remove("token").apply()
    }
}
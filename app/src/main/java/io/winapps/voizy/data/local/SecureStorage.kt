package io.winapps.voizy.data.local

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import io.winapps.voizy.ui.features.more.UserPreferences

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

    fun saveUserPrefs(userPrefs: UserPreferences) {
        prefs.edit()
            .putString("appPrimaryColor", userPrefs.primaryColor)
            .putString("appPrimaryAccent", userPrefs.primaryAccent)
            .putString("appSecondaryColor", userPrefs.secondaryColor)
            .putString("appSecondaryAccent", userPrefs.secondaryAccent)
            .putBoolean("songAutoplay", userPrefs.songAutoplay)
            .putString("profilePrimaryColor", userPrefs.profilePrimaryColor)
            .putString("profilePrimaryAccent", userPrefs.profilePrimaryAccent)
            .putString("profileSecondaryColor", userPrefs.profileSecondaryColor)
            .putString("profileSecondaryAccent", userPrefs.profileSecondaryAccent)
            .putBoolean("profileSongAutoplay", userPrefs.profileSongAutoplay)
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

    fun getUserPrefs(): UserPreferences {
        val storedUserPrefs = UserPreferences(
            primaryColor = prefs.getString("appPrimaryColor", null) ?: "yellow",
            primaryAccent = prefs.getString("appPrimaryAccent", null) ?: "pale-yellow",
            secondaryColor = prefs.getString("appSecondaryColor", null) ?: "magenta",
            secondaryAccent = prefs.getString("appSecondaryAccent", null) ?: "pale-magenta",
            songAutoplay = prefs.getBoolean("songAutoplay", false),
            profilePrimaryColor = prefs.getString("profilePrimaryColor", null) ?: "yellow",
            profilePrimaryAccent = prefs.getString("profilePrimaryAccent", null) ?: "pale-yellow",
            profileSecondaryColor = prefs.getString("profileSecondaryColor", null) ?: "magenta",
            profileSecondaryAccent = prefs.getString("profileSecondaryAccent", null) ?: "pale-magenta",
            profileSongAutoplay = prefs.getBoolean("profileSongAutoplay", false),
        )
        return storedUserPrefs
    }

    fun logout() {
        prefs.edit().remove("userId").apply()
        prefs.edit().remove("apiKey").apply()
        prefs.edit().remove("token").apply()
    }
}
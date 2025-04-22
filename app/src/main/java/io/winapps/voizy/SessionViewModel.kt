package io.winapps.voizy

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.winapps.voizy.data.local.SecureStorage
import io.winapps.voizy.ui.features.more.UserPreferences
import io.winapps.voizy.ui.theme.ColorMap
import io.winapps.voizy.ui.theme.ColorResourceMap
import io.winapps.voizy.ui.theme.getColorMap
import io.winapps.voizy.ui.theme.getColorResourceMap
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class AppScreen(val label: String) {
    HOME("home"),
    FEEDS("feeds"),
    GROUPS("groups"),
    PEOPLE("people"),
    PERSON("person"),
    NOTIFICATIONS("notifications"),
    PROFILE("profile"),
    MORE("more"),
}

@HiltViewModel
class SessionViewModel @Inject constructor(
    private val secureStorage: SecureStorage
) : ViewModel() {
    var userId by mutableStateOf<Long?>(null)
        private set

    var username by mutableStateOf<String?>(null)
        private set

    var preferredName by mutableStateOf<String?>(null)
        private set

    var profilePicURL by mutableStateOf<String?>(null)
        private set

    var userPrefs by mutableStateOf(UserPreferences())
        private set

    var appColors by mutableStateOf<ColorMap>(getColorMap(
        primaryColorString = userPrefs.primaryColor,
        primaryAccentString = userPrefs.primaryAccent,
        secondaryColorString = userPrefs.secondaryColor,
        secondaryAccentString = userPrefs.secondaryAccent
    ))
        private set

    var profileColors by mutableStateOf<ColorMap>(getColorMap(
        primaryColorString = userPrefs.profilePrimaryColor,
        primaryAccentString = userPrefs.profilePrimaryAccent,
        secondaryColorString = userPrefs.profileSecondaryColor,
        secondaryAccentString = userPrefs.profileSecondaryAccent
    ))
        private set

    var profilePlayerColors by mutableStateOf<ColorResourceMap>(getColorResourceMap())

    var didFinishSplash by mutableStateOf(false)
        private set

    var isLoggedIn by mutableStateOf(false)
        private set

    var currentAppScreen by mutableStateOf(AppScreen.HOME)
        private set

    init {
        viewModelScope.launch {
            val storedUserId = secureStorage.getUserId()
            val storedApiKey = secureStorage.getApiKey()
            val storedToken = secureStorage.getToken()
            val storedUserPrefs = secureStorage.getUserPrefs()
            appColors = getColorMap(
                primaryColorString = storedUserPrefs.primaryColor,
                primaryAccentString = storedUserPrefs.primaryAccent,
                secondaryColorString = storedUserPrefs.secondaryColor,
                secondaryAccentString = storedUserPrefs.secondaryAccent,
            )
            profileColors = getColorMap(
                primaryColorString = storedUserPrefs.profilePrimaryColor,
                primaryAccentString = storedUserPrefs.profilePrimaryAccent,
                secondaryColorString = storedUserPrefs.profileSecondaryColor,
                secondaryAccentString = storedUserPrefs.profileSecondaryAccent,
            )
            profilePlayerColors = getColorResourceMap(
                primaryColorString = storedUserPrefs.primaryColor,
                primaryAccentString = storedUserPrefs.primaryAccent,
                secondaryColorString = storedUserPrefs.secondaryColor,
                secondaryAccentString = storedUserPrefs.secondaryAccent,
            )

            if (storedUserId != null && storedApiKey != null && storedToken != null) {
                userId = storedUserId
                didFinishSplash = true
                isLoggedIn = true
                userPrefs = storedUserPrefs
            } else {
                delay(1000L)
                userPrefs = storedUserPrefs
                didFinishSplash = true
            }
        }
    }

    fun markLoggedIn() {
        isLoggedIn = true
    }

    fun markLoggedOut() {
        isLoggedIn = false
    }

    fun handleLogout() {
        markLoggedOut()
        secureStorage.logout()
    }

    fun setUserData(userId: Long, username: String, apiKey: String, token: String) {
        this.userId = userId
        this.username = username
        secureStorage.saveUserId(userId)
        secureStorage.saveApiKey(apiKey)
        secureStorage.saveToken(token)
    }

    fun setUserProfileData(preferredName: String) {
        this.preferredName = preferredName
    }

    fun setUserProfileImage(profilePicURL: String?) {
        this.profilePicURL = profilePicURL
    }

    fun getStoredUserId(): Long? {
        return secureStorage.getUserId()
    }

    fun getApiKey(): String? {
        return secureStorage.getApiKey()
    }

    fun getToken(): String? {
        return secureStorage.getToken()
    }

    fun switchCurrentAppScreen(screen: AppScreen) {
        this.currentAppScreen = screen
    }

    fun updateUserPrefs(prefs: UserPreferences) {
        this.appColors = getColorMap(
            primaryColorString = prefs.primaryColor,
            primaryAccentString = prefs.primaryAccent,
            secondaryColorString = prefs.secondaryColor,
            secondaryAccentString = prefs.secondaryAccent
        )
        this.profileColors = getColorMap(
            primaryColorString = prefs.profilePrimaryColor,
            primaryAccentString = prefs.profilePrimaryAccent,
            secondaryColorString = prefs.profileSecondaryColor,
            secondaryAccentString = prefs.profileSecondaryAccent
        )
        this.profilePlayerColors = getColorResourceMap(
            primaryColorString = prefs.primaryColor,
            primaryAccentString = prefs.primaryAccent,
            secondaryColorString = prefs.secondaryColor,
            secondaryAccentString = prefs.secondaryAccent
        )
        this.userPrefs = prefs
        secureStorage.saveUserPrefs(prefs)
    }
}
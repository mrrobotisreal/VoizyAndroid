package io.winapps.voizy.ui.features.more

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.winapps.voizy.data.local.SecureStorage
import io.winapps.voizy.data.model.users.PutUserPreferencesRequest
import io.winapps.voizy.data.repository.UsersRepository
import io.winapps.voizy.ui.theme.OceanicPrimaryAccent
import io.winapps.voizy.ui.theme.OceanicPrimaryColor
import io.winapps.voizy.ui.theme.OceanicSecondaryAccent
import io.winapps.voizy.ui.theme.OceanicSecondaryColor
import io.winapps.voizy.ui.theme.RoyalPrimaryAccent
import io.winapps.voizy.ui.theme.RoyalPrimaryColor
import io.winapps.voizy.ui.theme.RoyalSecondaryAccent
import io.winapps.voizy.ui.theme.RoyalSecondaryColor
import io.winapps.voizy.ui.theme.SunsetPrimaryAccent
import io.winapps.voizy.ui.theme.SunsetPrimaryColor
import io.winapps.voizy.ui.theme.SunsetSecondaryAccent
import io.winapps.voizy.ui.theme.SunsetSecondaryColor
import io.winapps.voizy.ui.theme.VoizyPrimaryAccent
import io.winapps.voizy.ui.theme.VoizyPrimaryColor
import io.winapps.voizy.ui.theme.VoizySecondaryAccent
import io.winapps.voizy.ui.theme.VoizySecondaryColor
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class AppTheme(val label: String) {
    VOIZY("Voizy"),
    OCEANIC("Oceanic"),
    ROYAL("Royal"),
    SUNSET("Sunset");

    companion object {
        val default = VOIZY
    }
}

data class UserPreferences(
    val primaryColor: String = "yellow",
    val primaryAccent: String = "pale-yellow",
    val secondaryColor: String = "magenta",
    val secondaryAccent: String = "pale-magenta",
    val songAutoplay: Boolean = false,
    val profilePrimaryColor: String = "yellow",
    val profilePrimaryAccent: String = "pale-yellow",
    val profileSecondaryColor: String = "magenta",
    val profileSecondaryAccent: String = "pale-magenta",
    val profileSongAutoplay: Boolean = false
)

@HiltViewModel
class MoreViewModel @Inject constructor(
    private val usersRepository: UsersRepository,
    private val secureStorage: SecureStorage
) : ViewModel() {
    var showAppPrefs by mutableStateOf(false)
        private set

    fun onOpenAppPrefs() {
        showProfilePrefs = false
        showAppPrefs = true
    }

    fun onCloseAppPrefs() {
        showAppPrefs = false
    }

    var selectedAppTheme by mutableStateOf<AppTheme>(AppTheme.VOIZY)
        private set

    var appPrimaryColor by mutableStateOf<Color>(VoizyPrimaryColor)
        private set

    var appPrimaryAccent by mutableStateOf<Color>(VoizyPrimaryAccent)
        private set

    var appSecondaryColor by mutableStateOf<Color>(VoizySecondaryColor)
        private set

    var appSecondaryAccent by mutableStateOf<Color>(VoizySecondaryAccent)
        private set

    fun onSelectAppTheme(newTheme: AppTheme) {
        selectedAppTheme = newTheme
        var primaryColorStr = "yellow"
        var primaryAccentStr = "pale-yellow"
        var secondaryColorStr = "magenta"
        var secondaryAccentStr = "pale-magenta"
        when(newTheme) {
            AppTheme.VOIZY -> {
                appPrimaryColor = VoizyPrimaryColor
                primaryColorStr = "yellow"
                appPrimaryAccent = VoizyPrimaryAccent
                primaryAccentStr = "pale-yellow"
                appSecondaryColor = VoizySecondaryColor
                secondaryColorStr = "magenta"
                appSecondaryAccent = VoizySecondaryAccent
                secondaryAccentStr = "pale-magenta"
            }
            AppTheme.OCEANIC -> {
                appPrimaryColor = OceanicPrimaryColor
                primaryColorStr = "oceanic-primary-color"
                appPrimaryAccent = OceanicPrimaryAccent
                primaryAccentStr = "oceanic-primary-accent"
                appSecondaryColor = OceanicSecondaryColor
                secondaryColorStr = "oceanic-secondary-color"
                appSecondaryAccent = OceanicSecondaryAccent
                secondaryAccentStr = "oceanic-secondary-accent"
            }
            AppTheme.ROYAL -> {
                appPrimaryColor = RoyalPrimaryColor
                primaryColorStr = "royal-primary-color"
                appPrimaryAccent = RoyalPrimaryAccent
                primaryAccentStr = "royal-primary-accent"
                appSecondaryColor = RoyalSecondaryColor
                secondaryColorStr = "royal-secondary-color"
                appSecondaryAccent = RoyalSecondaryAccent
                secondaryAccentStr = "royal-secondary-accent"
            }
            AppTheme.SUNSET -> {
                appPrimaryColor = SunsetPrimaryColor
                primaryColorStr = "sunset-primary-color"
                appPrimaryAccent = SunsetPrimaryAccent
                primaryAccentStr = "sunset-primary-accent"
                appSecondaryColor = SunsetSecondaryColor
                secondaryColorStr = "sunset-secondary-color"
                appSecondaryAccent = SunsetSecondaryAccent
                secondaryAccentStr = "sunset-secondary-accent"
            }
        }
        val newPrefs = UserPreferences(
            primaryColor = primaryColorStr,
            primaryAccent = primaryAccentStr,
            secondaryColor = secondaryColorStr,
            secondaryAccent = secondaryAccentStr,
            songAutoplay = userPreferences.songAutoplay,
            profilePrimaryColor = userPreferences.profilePrimaryColor,
            profilePrimaryAccent = userPreferences.profilePrimaryAccent,
            profileSecondaryColor = userPreferences.profileSecondaryColor,
            profileSecondaryAccent = userPreferences.profileSecondaryAccent,
            profileSongAutoplay = userPreferences.profileSongAutoplay,
        )
        userPreferences = newPrefs
    }

    var songAutoplay by mutableStateOf(false)
        private set

    fun onToggleSongAutoplay(bool: Boolean) {
        songAutoplay = bool
        val newPrefs = UserPreferences(
            primaryColor = userPreferences.primaryColor,
            primaryAccent = userPreferences.primaryAccent,
            secondaryColor = userPreferences.secondaryColor,
            secondaryAccent = userPreferences.secondaryAccent,
            songAutoplay = bool,
            profilePrimaryColor = userPreferences.profilePrimaryColor,
            profilePrimaryAccent = userPreferences.profilePrimaryAccent,
            profileSecondaryColor = userPreferences.profileSecondaryColor,
            profileSecondaryAccent = userPreferences.profileSecondaryAccent,
            profileSongAutoplay = userPreferences.profileSongAutoplay,
        )
        userPreferences = newPrefs
    }

    var showAppThemes by mutableStateOf(false)
        private set

    fun onShowAppThemes() {
        showAppThemes = true
    }

    fun onCloseAppThemes() {
        showAppThemes = false
    }

    var showProfilePrefs by mutableStateOf(false)
        private set

    fun onOpenProfilePrefs() {
        showAppPrefs = false
        showProfilePrefs = true
    }

    fun onCloseProfilePrefs() {
        showProfilePrefs = false
    }

    var selectedProfileTheme by mutableStateOf<AppTheme>(AppTheme.VOIZY)
        private set

    var profilePrimaryColor by mutableStateOf<Color>(VoizyPrimaryColor)
        private set

    var profilePrimaryAccent by mutableStateOf<Color>(VoizyPrimaryAccent)
        private set

    var profileSecondaryColor by mutableStateOf<Color>(VoizySecondaryColor)
        private set

    var profileSecondaryAccent by mutableStateOf<Color>(VoizySecondaryAccent)
        private set

    fun onSelectProfileTheme(newTheme: AppTheme) {
        selectedProfileTheme = newTheme
        var primaryColorStr = "yellow"
        var primaryAccentStr = "pale-yellow"
        var secondaryColorStr = "magenta"
        var secondaryAccentStr = "pale-magenta"
        when(newTheme) {
            AppTheme.VOIZY -> {
                profilePrimaryColor = VoizyPrimaryColor
                primaryColorStr = "yellow"
                profilePrimaryAccent = VoizyPrimaryAccent
                primaryAccentStr = "pale-yellow"
                profileSecondaryColor = VoizySecondaryColor
                secondaryColorStr = "magenta"
                profileSecondaryAccent = VoizySecondaryAccent
                secondaryAccentStr = "pale-magenta"
            }
            AppTheme.OCEANIC -> {
                profilePrimaryColor = OceanicPrimaryColor
                primaryColorStr = "oceanic-primary-color"
                profilePrimaryAccent = OceanicPrimaryAccent
                primaryAccentStr = "oceanic-primary-accent"
                profileSecondaryColor = OceanicSecondaryColor
                secondaryColorStr = "oceanic-secondary-color"
                profileSecondaryAccent = OceanicSecondaryAccent
                secondaryAccentStr = "oceanic-secondary-accent"
            }
            AppTheme.ROYAL -> {
                profilePrimaryColor = RoyalPrimaryColor
                primaryColorStr = "royal-primary-color"
                profilePrimaryAccent = RoyalPrimaryAccent
                primaryAccentStr = "royal-primary-accent"
                profileSecondaryColor = RoyalSecondaryColor
                secondaryColorStr = "royal-secondary-color"
                profileSecondaryAccent = RoyalSecondaryAccent
                secondaryAccentStr = "royal-secondary-accent"
            }
            AppTheme.SUNSET -> {
                profilePrimaryColor = SunsetPrimaryColor
                primaryColorStr = "sunset-primary-color"
                profilePrimaryAccent = SunsetPrimaryAccent
                primaryAccentStr = "sunset-primary-accent"
                profileSecondaryColor = SunsetSecondaryColor
                secondaryColorStr = "sunset-secondary-color"
                profileSecondaryAccent = SunsetSecondaryAccent
                secondaryAccentStr = "sunset-secondary-accent"
            }
        }
        val newPrefs = UserPreferences(
            primaryColor = userPreferences.primaryColor,
            primaryAccent = userPreferences.primaryAccent,
            secondaryColor = userPreferences.secondaryColor,
            secondaryAccent = userPreferences.secondaryAccent,
            songAutoplay = userPreferences.songAutoplay,
            profilePrimaryColor = primaryColorStr,
            profilePrimaryAccent = primaryAccentStr,
            profileSecondaryColor = secondaryColorStr,
            profileSecondaryAccent = secondaryAccentStr,
            profileSongAutoplay = userPreferences.profileSongAutoplay,
        )
        userPreferences = newPrefs
    }

    var profileSongAutoplay by mutableStateOf(false)
        private set

    fun onToggleProfileSongAutoplay(bool: Boolean) {
        profileSongAutoplay = bool
        val newPrefs = UserPreferences(
            primaryColor = userPreferences.primaryColor,
            primaryAccent = userPreferences.primaryAccent,
            secondaryColor = userPreferences.secondaryColor,
            secondaryAccent = userPreferences.secondaryAccent,
            songAutoplay = userPreferences.songAutoplay,
            profilePrimaryColor = userPreferences.profilePrimaryColor,
            profilePrimaryAccent = userPreferences.profilePrimaryAccent,
            profileSecondaryColor = userPreferences.profileSecondaryColor,
            profileSecondaryAccent = userPreferences.profileSecondaryAccent,
            profileSongAutoplay = bool,
        )
        userPreferences = newPrefs
    }

    var showProfileThemes by mutableStateOf(false)
        private set

    fun onShowProfileThemes() {
        showProfileThemes = true
    }

    fun onCloseProfileThemes() {
        showProfileThemes = false
    }

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var userPreferences by mutableStateOf<UserPreferences>(UserPreferences())
        private set

    fun loadUserPreferences(apiKey: String, userId: Long, updateStoredUserPrefs: (UserPreferences) -> Unit) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            try {
                val response = usersRepository.getUserPreferences(
                    apiKey = apiKey,
                    userIdHeader = userId.toString(),
                    userId = userId,
                )

                val loadedPrefs = UserPreferences(
                    primaryColor = response.primaryColor,
                    primaryAccent = response.primaryAccent,
                    secondaryColor = response.secondaryColor,
                    secondaryAccent = response.secondaryAccent,
                    songAutoplay = response.songAutoplay,
                    profilePrimaryColor = response.profilePrimaryColor,
                    profilePrimaryAccent = response.profilePrimaryAccent,
                    profileSecondaryColor = response.profileSecondaryColor,
                    profileSecondaryAccent = response.profileSecondaryAccent,
                    profileSongAutoplay = response.profileSongAutoplay
                )
                userPreferences = loadedPrefs

                when(response.primaryColor) {
                    "yellow" -> {
                        appPrimaryColor = VoizyPrimaryColor
                    }
                    "oceanic-primary-color" -> {
                        appPrimaryColor = OceanicPrimaryColor
                    }
                    "royal-primary-color" -> {
                        appPrimaryColor = RoyalPrimaryColor
                    }
                    "sunset-primary-color" -> {
                        appPrimaryColor = SunsetPrimaryColor
                    }
                    else -> {
                        appPrimaryColor = VoizyPrimaryColor
                    }
                }

                when(response.primaryAccent) {
                    "pale-yellow" -> {
                        appPrimaryAccent = VoizyPrimaryAccent
                    }
                    "oceanic-primary-accent" -> {
                        appPrimaryAccent = OceanicPrimaryAccent
                    }
                    "royal-primary-accent" -> {
                        appPrimaryAccent = RoyalPrimaryAccent
                    }
                    "sunset-primary-accent" -> {
                        appPrimaryAccent = SunsetPrimaryAccent
                    }
                    else -> {
                        appPrimaryAccent = VoizyPrimaryAccent
                    }
                }

                when(response.secondaryColor) {
                    "magenta" -> {
                        appSecondaryColor = VoizySecondaryColor
                    }
                    "oceanic-secondary-color" -> {
                        appSecondaryColor = OceanicSecondaryColor
                    }
                    "royal-secondary-color" -> {
                        appSecondaryColor = RoyalSecondaryColor
                    }
                    "sunset-secondary-color" -> {
                        appSecondaryColor = SunsetSecondaryColor
                    }
                    else -> {
                        appSecondaryColor = VoizySecondaryColor
                    }
                }

                when(response.secondaryAccent) {
                    "pale-magenta" -> {
                        appSecondaryAccent = VoizySecondaryAccent
                    }
                    "oceanic-secondary-accent" -> {
                        appSecondaryAccent = OceanicSecondaryAccent
                    }
                    "royal-secondary-accent" -> {
                        appSecondaryAccent = RoyalSecondaryAccent
                    }
                    "sunset-secondary-accent" -> {
                        appSecondaryAccent = SunsetSecondaryAccent
                    }
                    else -> {
                        appSecondaryAccent = VoizySecondaryAccent
                    }
                }

                songAutoplay = when(response.songAutoplay) {
                    true -> {
                        true
                    }

                    false -> {
                        false
                    }

                    else -> {
                        false
                    }
                }

                when(response.profilePrimaryColor) {
                    "yellow" -> {
                        profilePrimaryColor = VoizyPrimaryColor
                    }
                    "oceanic-primary-color" -> {
                        profilePrimaryColor = OceanicPrimaryColor
                    }
                    "royal-primary-color" -> {
                        profilePrimaryColor = RoyalPrimaryColor
                    }
                    "sunset-primary-color" -> {
                        profilePrimaryColor = SunsetPrimaryColor
                    }
                    else -> {
                        profilePrimaryColor = VoizyPrimaryColor
                    }
                }

                when(response.profilePrimaryAccent) {
                    "pale-yellow" -> {
                        profilePrimaryAccent = VoizyPrimaryAccent
                    }
                    "oceanic-primary-accent" -> {
                        profilePrimaryAccent = OceanicPrimaryAccent
                    }
                    "royal-primary-accent" -> {
                        profilePrimaryAccent = RoyalPrimaryAccent
                    }
                    "sunset-primary-accent" -> {
                        profilePrimaryAccent = SunsetPrimaryAccent
                    }
                    else -> {
                        profilePrimaryAccent = VoizyPrimaryAccent
                    }
                }

                when(response.profileSecondaryColor) {
                    "magenta" -> {
                        profileSecondaryColor = VoizySecondaryColor
                    }
                    "oceanic-secondary-color" -> {
                        profileSecondaryColor = OceanicSecondaryColor
                    }
                    "royal-secondary-color" -> {
                        profileSecondaryColor = RoyalSecondaryColor
                    }
                    "sunset-secondary-color" -> {
                        profileSecondaryColor = SunsetSecondaryColor
                    }
                    else -> {
                        profileSecondaryColor = VoizySecondaryColor
                    }
                }

                when(response.profileSecondaryAccent) {
                    "pale-magenta" -> {
                        profileSecondaryAccent = VoizySecondaryAccent
                    }
                    "oceanic-secondary-accent" -> {
                        profileSecondaryAccent = OceanicSecondaryAccent
                    }
                    "royal-secondary-accent" -> {
                        profileSecondaryAccent = RoyalSecondaryAccent
                    }
                    "sunset-secondary-accent" -> {
                        profileSecondaryAccent = SunsetSecondaryAccent
                    }
                    else -> {
                        profileSecondaryAccent = VoizySecondaryAccent
                    }
                }

                profileSongAutoplay = when(response.profileSongAutoplay) {
                    true -> {
                        true
                    }

                    false -> {
                        false
                    }

                    else -> {
                        false
                    }
                }

                updateStoredUserPrefs(loadedPrefs)
            } catch (e: Exception) {
                errorMessage = e.message
            } finally {
                isLoading = false
            }
        }
    }

    var isUpdatingPrefs by mutableStateOf(false)
        private set

    var showUpdatePrefsSuccessToast by mutableStateOf(false)
        private set

    fun onUpdateAppPrefs(apiKey: String, userId: Long, token: String, onClose: () -> Unit, updateStoredUserPrefs: (UserPreferences) -> Unit) {
        viewModelScope.launch {
            isUpdatingPrefs = true

            try {
                val response = usersRepository.putUserPreferences(
                    apiKey = apiKey,
                    userIdHeader = userId.toString(),
                    token = "Bearer $token",
                    putUserPreferencesRequest = PutUserPreferencesRequest(
                        userID = userId,
                        primaryColor = userPreferences.primaryColor,
                        primaryAccent = userPreferences.primaryAccent,
                        secondaryColor = userPreferences.secondaryColor,
                        secondaryAccent = userPreferences.secondaryAccent,
                        songAutoplay = userPreferences.songAutoplay,
                        profilePrimaryColor = null,
                        profilePrimaryAccent = null,
                        profileSecondaryColor = null,
                        profileSecondaryAccent = null,
                        profileSongAutoplay = null,
                    )
                )

                if (response.success) {
                    loadUserPreferences(
                        apiKey = apiKey,
                        userId = userId,
                        updateStoredUserPrefs = updateStoredUserPrefs
                    )
                    showUpdatePrefsSuccessToast = true
                }
            } catch (e: Exception) {
                //
            } finally {
                isUpdatingPrefs = false
                onClose()
            }
        }
    }

    fun endShowUpdatePrefsSuccessToast() {
        showUpdatePrefsSuccessToast = false
    }

    fun onUpdateProfilePrefs(apiKey: String, userId: Long, token: String, onClose: () -> Unit, updateStoredUserPrefs: (UserPreferences) -> Unit) {
        viewModelScope.launch {
            isUpdatingPrefs = true

            try {
                val response = usersRepository.putUserPreferences(
                    apiKey = apiKey,
                    userIdHeader = userId.toString(),
                    token = "Bearer $token",
                    putUserPreferencesRequest = PutUserPreferencesRequest(
                        userID = userId,
                        primaryColor = null,
                        primaryAccent = null,
                        secondaryColor = null,
                        secondaryAccent = null,
                        songAutoplay = null,
                        profilePrimaryColor = userPreferences.profilePrimaryColor,
                        profilePrimaryAccent = userPreferences.profilePrimaryAccent,
                        profileSecondaryColor = userPreferences.profileSecondaryColor,
                        profileSecondaryAccent = userPreferences.profileSecondaryAccent,
                        profileSongAutoplay = userPreferences.profileSongAutoplay,
                    )
                )

                if (response.success) {
                    loadUserPreferences(
                        apiKey = apiKey,
                        userId = userId,
                        updateStoredUserPrefs = updateStoredUserPrefs
                    )
                    showUpdatePrefsSuccessToast = true
                }
            } catch (e: Exception) {
                //
            } finally {
                isUpdatingPrefs = false
                onClose()
            }
        }
    }
}
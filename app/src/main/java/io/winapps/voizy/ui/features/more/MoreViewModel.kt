package io.winapps.voizy.ui.features.more

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
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
    private val usersRepository: UsersRepository
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
        when(newTheme) {
            AppTheme.VOIZY -> {
                appPrimaryColor = VoizyPrimaryColor
                appPrimaryAccent = VoizyPrimaryAccent
                appSecondaryColor = VoizySecondaryColor
                appSecondaryAccent = VoizySecondaryAccent
            }
            AppTheme.OCEANIC -> {
                appPrimaryColor = OceanicPrimaryColor
                appPrimaryAccent = OceanicPrimaryAccent
                appSecondaryColor = OceanicSecondaryColor
                appSecondaryAccent = OceanicSecondaryAccent
            }
            AppTheme.ROYAL -> {
                appPrimaryColor = RoyalPrimaryColor
                appPrimaryAccent = RoyalPrimaryAccent
                appSecondaryColor = RoyalSecondaryColor
                appSecondaryAccent = RoyalSecondaryAccent
            }
            AppTheme.SUNSET -> {
                appPrimaryColor = SunsetPrimaryColor
                appPrimaryAccent = SunsetPrimaryAccent
                appSecondaryColor = SunsetSecondaryColor
                appSecondaryAccent = SunsetSecondaryAccent
            }
        }
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
        when(newTheme) {
            AppTheme.VOIZY -> {
                profilePrimaryColor = VoizyPrimaryColor
                profilePrimaryAccent = VoizyPrimaryAccent
                profileSecondaryColor = VoizySecondaryColor
                profileSecondaryAccent = VoizySecondaryAccent
            }
            AppTheme.OCEANIC -> {
                profilePrimaryColor = OceanicPrimaryColor
                profilePrimaryAccent = OceanicPrimaryAccent
                profileSecondaryColor = OceanicSecondaryColor
                profileSecondaryAccent = OceanicSecondaryAccent
            }
            AppTheme.ROYAL -> {
                profilePrimaryColor = RoyalPrimaryColor
                profilePrimaryAccent = RoyalPrimaryAccent
                profileSecondaryColor = RoyalSecondaryColor
                profileSecondaryAccent = RoyalSecondaryAccent
            }
            AppTheme.SUNSET -> {
                profilePrimaryColor = SunsetPrimaryColor
                profilePrimaryAccent = SunsetPrimaryAccent
                profileSecondaryColor = SunsetSecondaryColor
                profileSecondaryAccent = SunsetSecondaryAccent
            }
        }
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

    fun loadUserPreferences(apiKey: String, userId: Long) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            try {
                val response = usersRepository.getUserPreferences(
                    apiKey = apiKey,
                    userIdHeader = userId.toString(),
                    userId = userId,
                )
                userPreferences = UserPreferences(
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
            } catch (e: Exception) {
                errorMessage = e.message
            } finally {
                isLoading = false
            }
        }
    }

    var isUpdatingPrefs by mutableStateOf(false)
        private set

    fun onUpdateAppPrefs(apiKey: String, userId: Long, token: String, onClose: () -> Unit) {
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
                    // Show toast
                    loadUserPreferences(
                        apiKey = apiKey,
                        userId = userId
                    )
                }
            } catch (e: Exception) {
                //
            } finally {
                onClose()
            }
        }
    }
}
package io.winapps.voizy

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.winapps.voizy.data.local.SecureStorage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

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

    var didFinishSplash by mutableStateOf(false)
        private set

    var isLoggedIn by mutableStateOf(false)
        private set

    init {
        viewModelScope.launch {
            delay(1000L)
            didFinishSplash = true
        }
    }

    fun markLoggedIn() {
        isLoggedIn = true
    }

    fun markLoggedOut() {
        isLoggedIn = false
    }

    fun setUserData(userId: Long, username: String, apiKey: String, token: String) {
        this.userId = userId
        this.username = username
        secureStorage.saveApiKey(apiKey)
        secureStorage.saveToken(token)
    }

    fun setUserProfileData(preferredName: String) {
        this.preferredName = preferredName
    }

    fun getApiKey(): String? {
        return secureStorage.getApiKey()
    }

    fun getToken(): String? {
        return secureStorage.getToken()
    }
}
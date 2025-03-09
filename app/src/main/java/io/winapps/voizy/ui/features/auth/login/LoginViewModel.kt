package io.winapps.voizy.ui.features.auth.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.winapps.voizy.data.model.auth.LoginResponse
import io.winapps.voizy.data.repository.AuthRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    var isRegistering by mutableStateOf(false)
        private set

    var username by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    var email by mutableStateOf("")
        private set

    var preferredName by mutableStateOf("")
        private set

    var loginState by mutableStateOf<LoginState>(LoginState.Idle)

    fun onUsernameChanged(newValue: String) {
        username = newValue
    }

    fun onPasswordChanged(newValue: String) {
        password = newValue
    }

    fun onEmailChanged(newValue: String) {
        email = newValue
    }

    fun onPreferredNameChanged(newValue: String) {
        preferredName = newValue
    }

    fun toggleRegistration() {
        isRegistering = !isRegistering
        username = ""
        password = ""
        email = ""
        preferredName = ""
    }

    fun onLogin() {
        viewModelScope.launch {
            loginState = LoginState.Loading

            try {
                val response = authRepository.login(username, password)

                loginState = LoginState.Success(response)
            } catch (e: Exception) {
                loginState = LoginState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun onCreateAccount() {
        // TODO: make call to create new account
    }
}

sealed class LoginState {
    data object Idle : LoginState()
    data object Loading : LoginState()
    data class Success(val response: LoginResponse) : LoginState()
    data class Error(val message: String) : LoginState()
}

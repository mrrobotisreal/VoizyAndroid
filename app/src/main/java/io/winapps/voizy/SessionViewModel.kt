package io.winapps.voizy

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SessionViewModel @Inject constructor() : ViewModel() {
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
}
package io.winapps.voizy.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {
    var searchText by mutableStateOf("")
        private set

    fun onSearchTextChanged(newValue: String) {
        searchText = newValue
    }
}
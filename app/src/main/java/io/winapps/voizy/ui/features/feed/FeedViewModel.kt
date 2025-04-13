package io.winapps.voizy.ui.features.feed

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

enum class FeedTab(val label: String) {
    FOR_YOU("For You"),
    POPULAR("Popular"),
    GROUPS("Groups"),
    FRIENDS("Friends");

    companion object {
        val default = FOR_YOU
    }
}

@HiltViewModel
class FeedViewModel @Inject constructor() : ViewModel() {
    var searchText by mutableStateOf("")
        private set

    var showFiltersDialog by mutableStateOf(false)
        private set

    var selectedFilter by mutableStateOf<FeedTab>(FeedTab.FOR_YOU)
        private set

    var selectedFilterLabel by mutableStateOf("For You feed")
        private set

    fun onSearchTextChanged(newValue: String) {
        searchText = newValue
    }

    fun onOpenFiltersDialog() {
        showFiltersDialog = true
    }

    fun onCloseFiltersDialog() {
        showFiltersDialog = false
    }

    fun onSelectFilter(newFilter: FeedTab) {
        selectedFilter = newFilter
        selectedFilterLabel = when(newFilter) {
            FeedTab.FOR_YOU -> "For You feed"
            FeedTab.POPULAR -> "Popular feed"
            FeedTab.GROUPS -> "Groups feed"
            FeedTab.FRIENDS -> "Friends feed"
        }
    }
}
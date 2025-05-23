package io.winapps.voizy.ui.features.feed

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.winapps.voizy.SessionViewModel

@Composable
fun FeedTabsRow(
    selectedTab: FeedTab,
    onTabSelected: (FeedTab) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        TabButton(tab = FeedTab.FOR_YOU, selectedTab, onTabSelected, "For You")
        TabButton(tab = FeedTab.POPULAR, selectedTab, onTabSelected, "Popular")
        TabButton(tab = FeedTab.GROUPS, selectedTab, onTabSelected, "Groups")
        TabButton(tab = FeedTab.FRIENDS, selectedTab, onTabSelected, "Friends")
    }
}

@Composable
fun TabButton(
    tab: FeedTab,
    selectedTab: FeedTab,
    onTabSelected: (FeedTab) -> Unit,
    label: String
) {
    val sessionViewModel = hiltViewModel<SessionViewModel>()
    val secondaryColor = sessionViewModel.appColors.secondaryColor
    val secondaryAccent = sessionViewModel.appColors.secondaryAccent
    val isSelected = (tab == selectedTab)
    val backgroundColor = if (isSelected) secondaryColor else secondaryAccent
    val textColor = if (isSelected) secondaryAccent else secondaryColor

    TextButton(
        onClick = { onTabSelected(tab) },
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor),
        border = BorderStroke(1.dp, secondaryColor)
    ) {
        Text(
            text = label,
            color = textColor
        )
    }
}
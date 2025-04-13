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
    val isSelected = (tab == selectedTab)
    val backgroundColor = if (isSelected) Color(0xFFF10E91) else Color(0xFFFFD5ED)
    val textColor = if (isSelected) Color(0xFFFFD5ED) else Color(0xFFF10E91)

    TextButton(
        onClick = { onTabSelected(tab) },
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor),
        border = BorderStroke(1.dp, Color(0xFFF10E91))
    ) {
        Text(
            text = label,
            color = textColor
        )
    }
}
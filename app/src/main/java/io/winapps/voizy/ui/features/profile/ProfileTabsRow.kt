package io.winapps.voizy.ui.features.profile

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
fun ProfileTabsRow(
    selectedTab: ProfileTab,
    onTabSelected: (ProfileTab) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        TabButton(tab = ProfileTab.POSTS, selectedTab, onTabSelected, "Posts")
        TabButton(tab = ProfileTab.PHOTOS, selectedTab, onTabSelected, "Photos")
        TabButton(tab = ProfileTab.ABOUT, selectedTab, onTabSelected, "About")
        TabButton(tab = ProfileTab.FRIENDS, selectedTab, onTabSelected, "Friends")
    }
}

@Composable
fun TabButton(
    tab: ProfileTab,
    selectedTab: ProfileTab,
    onTabSelected: (ProfileTab) -> Unit,
    label: String
) {
    val isSelected = (tab == selectedTab)
    val backgroundColor = if (isSelected) Color(0xFFF10E91) else Color(0xFFFFD5ED)
    val textColor = if (isSelected) Color(0xFFFFD5ED) else Color(0xFFF10E91)

    TextButton(
        onClick = { onTabSelected(tab) },
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor)
    ) {
        Text(
            text = label,
            color = textColor
        )
    }
}

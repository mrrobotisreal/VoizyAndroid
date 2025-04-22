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
import androidx.hilt.navigation.compose.hiltViewModel
import io.winapps.voizy.SessionViewModel

@Composable
fun ProfileTabsRow(
    selectedTab: ProfileTab,
    onTabSelected: (ProfileTab) -> Unit,
    secondaryColor: Color,
    secondaryAccent: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        TabButton(
            tab = ProfileTab.POSTS,
            selectedTab,
            onTabSelected,
            "Posts",
            secondaryColor,
            secondaryAccent
        )
        TabButton(
            tab = ProfileTab.PHOTOS,
            selectedTab,
            onTabSelected,
            "Photos",
            secondaryColor,
            secondaryAccent
        )
        TabButton(
            tab = ProfileTab.ABOUT,
            selectedTab,
            onTabSelected,
            "About",
            secondaryColor,
            secondaryAccent
        )
        TabButton(
            tab = ProfileTab.FRIENDS,
            selectedTab,
            onTabSelected,
            "Friends",
            secondaryColor,
            secondaryAccent
        )
    }
}

@Composable
fun TabButton(
    tab: ProfileTab,
    selectedTab: ProfileTab,
    onTabSelected: (ProfileTab) -> Unit,
    label: String,
    secondaryColor: Color,
    secondaryAccent: Color
) {
    val isSelected = (tab == selectedTab)
    val backgroundColor = if (isSelected) secondaryColor else secondaryAccent
    val textColor = if (isSelected) secondaryAccent else secondaryColor

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

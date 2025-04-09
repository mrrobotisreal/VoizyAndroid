package io.winapps.voizy.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Feed
import androidx.compose.material.icons.filled.Feed
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.winapps.voizy.AppScreen
import io.winapps.voizy.SessionViewModel

@Composable
fun BottomNavBar(
    sessionViewModel: SessionViewModel = hiltViewModel(),
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Divider(
            color = Color(0xFFF10E91),
            modifier = Modifier
                .height(1.dp)
                .fillMaxWidth()
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .background(Color(0xFFF9D841))
//            .background(Color.Transparent)
                .navigationBarsPadding(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 6 icons: Home, Feed, Groups, Notifications, Profile, More
            NavIconButton(
                icon = Icons.Default.Home,
                label = "Home",
                isSelected = sessionViewModel.currentAppScreen == AppScreen.HOME,
                onClick = { sessionViewModel.switchCurrentAppScreen(AppScreen.HOME) }
            )
            NavIconButton(
                icon = Icons.AutoMirrored.Filled.Feed,
                label = "Feed",
                isSelected = sessionViewModel.currentAppScreen == AppScreen.FEEDS,
                onClick = { sessionViewModel.switchCurrentAppScreen(AppScreen.FEEDS) }
            )
            NavIconButton(
                icon = Icons.Filled.Groups,
                label = "Groups",
                isSelected = sessionViewModel.currentAppScreen == AppScreen.GROUPS,
                onClick = { sessionViewModel.switchCurrentAppScreen(AppScreen.GROUPS) }
            )
            NavIconButton(
                icon = Icons.Filled.Notifications,
                label = "Notifications",
                isSelected = sessionViewModel.currentAppScreen == AppScreen.NOTIFICATIONS,
                onClick = { sessionViewModel.switchCurrentAppScreen(AppScreen.NOTIFICATIONS) }
            )
            NavIconButton(
                icon = Icons.Default.Person,
                label = "Profile",
                isSelected = sessionViewModel.currentAppScreen == AppScreen.PROFILE,
                onClick = { sessionViewModel.switchCurrentAppScreen(AppScreen.PROFILE) }
            )
            NavIconButton(
                icon = Icons.Filled.MoreVert,
                label = "More",
                isSelected = sessionViewModel.currentAppScreen == AppScreen.MORE,
                onClick = { sessionViewModel.switchCurrentAppScreen(AppScreen.MORE) }
            )
        }
    }
}

@Composable
fun NavIconButton(
    icon: ImageVector,
    label: String,
    isSelected: Boolean = false,
    onClick: () -> Unit
    ) {
    val backgroundColor = if (isSelected) Color(0xFFF10E91) else Color(0xFFFFD5ED)
    val iconTint = if (isSelected) Color(0xFFFFD5ED) else Color(0xFFF10E91)

    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .border(1.dp, Color(0xFFF10E91), CircleShape),
        colors = IconButtonDefaults.iconButtonColors(containerColor = backgroundColor)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = iconTint
        )
    }
}
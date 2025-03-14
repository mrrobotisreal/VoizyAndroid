package io.winapps.voizy.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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

@Composable
fun BottomNavBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(Color.Transparent)
            .navigationBarsPadding(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 6 icons: Home, Feed, Groups, Notifications, Profile, More
        NavIconButton(Icons.Default.Home, "Home")
        NavIconButton(Icons.AutoMirrored.Filled.Feed, "Feed")
        NavIconButton(Icons.Filled.Groups, "Groups")
        NavIconButton(Icons.Filled.Notifications, "Notifications")
        NavIconButton(Icons.Default.Person, "Profile")
        NavIconButton(Icons.Filled.MoreVert, "More")
    }
}

@Composable
fun NavIconButton(icon: ImageVector, label: String) {
    IconButton(
        onClick = { /* TODO: handle nav action */ },
        modifier = Modifier.size(48.dp).clip(CircleShape),
        colors = IconButtonDefaults.iconButtonColors(containerColor = Color(0xFFF10E91))
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = Color(0xFFFFD5ED)
        )
    }
}
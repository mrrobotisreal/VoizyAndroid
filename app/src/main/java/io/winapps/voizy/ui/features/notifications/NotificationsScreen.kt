package io.winapps.voizy.ui.features.notifications

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MarkChatRead
import androidx.compose.material.icons.filled.MarkChatUnread
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import io.winapps.voizy.data.model.notifications.FormattedNotification
import io.winapps.voizy.data.model.notifications.NotificationType
import io.winapps.voizy.ui.navigation.BottomNavBar
import io.winapps.voizy.ui.theme.Ubuntu

@Composable
fun NotificationsScreen() {
    val notificationsViewModel = hiltViewModel<NotificationsViewModel>()
    val view = LocalView.current

    SideEffect {
        val activity = view.context as Activity
        activity.window.statusBarColor = Color(0xFFF9D841).toArgb()
        WindowCompat.getInsetsController(activity.window, view).isAppearanceLightStatusBars = true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFDF4C9))
            .statusBarsPadding()
            .imePadding()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF9D841))
                .align(Alignment.CenterHorizontally),
        ) {
            Row(
                modifier = Modifier.padding(4.dp).fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Filled.Notifications,
                    contentDescription = "Notifications bell",
                    tint = Color(0xFFF10E91)
                )

                Text(
                    text = "Notifications",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontFamily = Ubuntu,
                        fontWeight = FontWeight.Bold
                    ),
                    textAlign = TextAlign.Center
                )
            }
        }

        Divider(
            color = Color(0xFFF10E91),
            modifier = Modifier
                .height(1.dp)
                .fillMaxWidth()
        )

        Box(
            modifier = Modifier.weight(1f).padding(4.dp)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(notificationsViewModel.formattedNotifications) { notification ->
                    NotificationCard(
                        notification = notification,
                        onClick = {}
                    )
                }
            }
        }

        BottomNavBar()
    }
}

@Composable
fun NotificationCard(
    notification: FormattedNotification,
    onClick: (FormattedNotification) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .border(1.dp, Color(0xFFF10E91), RoundedCornerShape(12.dp))
            .clickable { onClick(notification) },
        elevation = CardDefaults.cardElevation(7.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize().background(Color.White).clip(RoundedCornerShape(12.dp))
        ) {
            Row(
                modifier = Modifier.padding(4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Absolute.SpaceEvenly
            ) {
                if (notification.isRead) {
                    Icon(
                        imageVector = Icons.Filled.MarkChatRead,
                        contentDescription = "Read",
                        tint = Color.Green
                    )
                } else {
                    Icon(
                        imageVector = Icons.Filled.MarkChatUnread,
                        contentDescription = "Unread",
                        tint = Color.Red
                    )
                }

                val senderName = if (!notification.senderPreferredName.isNullOrBlank() && !notification.senderLastName.isNullOrBlank()) {
                    "${notification.senderPreferredName} ${notification.senderLastName}"
                } else if (!notification.senderPreferredName.isNullOrBlank()) {
                    "${notification.senderPreferredName}"
                } else {
                    "${notification.senderUsername}"
                }
                val notificationType = when(notification.notificationType) {
                    NotificationType.REACTION -> "reacted to your post"
                    NotificationType.COMMENT -> "commented on your post"
                    NotificationType.TAG -> "tagged in their post"
                    NotificationType.GROUP_INVITE -> "invited you to a group"
                    NotificationType.FRIEND_REQUEST -> "sent you a friend request"
                    NotificationType.GROUP_ACCEPTED -> "accepted your group invitation"
                    NotificationType.FRIEND_ACCEPTED -> "accepted your friend request"
                }
                Text(
                    text = "$senderName $notificationType",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontFamily = Ubuntu,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}
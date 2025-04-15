package io.winapps.voizy.ui.features.notifications

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.winapps.voizy.data.model.notifications.FormattedNotification
import io.winapps.voizy.data.model.notifications.Notification
import io.winapps.voizy.data.model.notifications.NotificationType
import io.winapps.voizy.data.model.notifications.ResourceType
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor() : ViewModel() {
    var notifications by mutableStateOf<List<Notification>>(listOf(
        Notification(
            notificationID = 1,
            userID = 1,
            senderID = 3,
            notificationType = NotificationType.REACTION,
            relatedResourceType = ResourceType.POST,
            relatedResourceID = 13,
            isRead = true,
        ),
        Notification(
            notificationID = 2,
            userID = 1,
            senderID = 2,
            notificationType = NotificationType.REACTION,
            relatedResourceType = ResourceType.POST,
            relatedResourceID = 1,
            isRead = false,
        ),
        Notification(
            notificationID = 3,
            userID = 1,
            senderID = 8,
            notificationType = NotificationType.REACTION,
            relatedResourceType = ResourceType.POST,
            relatedResourceID = 1,
            isRead = false,
        ),
    ))
        private set

    var formattedNotifications by mutableStateOf<List<FormattedNotification>>(listOf(
        FormattedNotification(
            notificationID = 1,
            senderID = 3,
            senderUsername = "alina.ranok",
            senderPreferredName = "Alina",
            senderFirstName = "Alina",
            senderLastName = "Wintrow",
            notificationType = NotificationType.REACTION,
            relatedResourceType = ResourceType.POST,
            relatedResourceID = 1,
            isRead = true
        ),
        FormattedNotification(
            notificationID = 1,
            senderID = 2,
            senderUsername = "Yoshishige",
            senderPreferredName = "Owen",
            senderFirstName = "Owen",
            senderLastName = "Yoshishige",
            notificationType = NotificationType.REACTION,
            relatedResourceType = ResourceType.POST,
            relatedResourceID = 1,
            isRead = false
        ),
        FormattedNotification(
            notificationID = 1,
            senderID = 8,
            senderUsername = "rayray",
            senderPreferredName = "Raymond",
            senderFirstName = "Raymond",
            senderLastName = "Yee",
            notificationType = NotificationType.REACTION,
            relatedResourceType = ResourceType.POST,
            relatedResourceID = 1,
            isRead = false
        ),
    ))
}
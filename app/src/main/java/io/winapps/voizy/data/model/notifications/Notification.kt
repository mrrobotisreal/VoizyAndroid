package io.winapps.voizy.data.model.notifications

enum class NotificationType(val label: String) {
    REACTION("reaction"),
    COMMENT("comment"),
    TAG("tag"),
    FRIEND_REQUEST("friend_request"),
    FRIEND_ACCEPTED("friend_accepted"),
    GROUP_INVITE("group_invite"),
    GROUP_ACCEPTED("group_accepted"),
}

enum class ResourceType(val label: String) {
    POST("post"),
    COMMENT("comment"),
    USER("user"),
    GROUP("group")
}

data class Notification(
    val notificationID: Long,
    val userID: Long,
    val senderID: Long?,
    val notificationType: NotificationType,
    val relatedResourceType: ResourceType,
    val relatedResourceID: Long,
    val isRead: Boolean
)

data class FormattedNotification(
    val notificationID: Long,
    val senderID: Long?,
    val senderUsername: String?,
    val senderPreferredName: String?,
    val senderFirstName: String?,
    val senderLastName: String?,
    val notificationType: NotificationType,
    val relatedResourceType: ResourceType,
    val relatedResourceID: Long,
    val isRead: Boolean
)
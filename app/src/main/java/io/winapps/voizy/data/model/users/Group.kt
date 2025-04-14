package io.winapps.voizy.data.model.users

enum class GroupPrivacy(val label: String) {
    PUBLIC("Public"),
    PRIVATE("Private"),
    CLOSED("Closed")
}

data class Group(
    val groupID: Long,
    val name: String,
    val description: String,
    val privacy: GroupPrivacy,
    val creatorID: Long,
    val createdAt: String,
    val totalUsers: Long,
    val imageURL: String?
)
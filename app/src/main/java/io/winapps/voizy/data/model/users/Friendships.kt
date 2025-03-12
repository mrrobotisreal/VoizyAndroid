package io.winapps.voizy.data.model.users

data class Friend(
    val userId: Long,
    val firstName: String?,
    val lastName: String?,
    val preferredName: String,
    val username: String,
    val profileImageUrl: String?
)
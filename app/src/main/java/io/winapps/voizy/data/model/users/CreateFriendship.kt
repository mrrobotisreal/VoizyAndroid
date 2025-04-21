package io.winapps.voizy.data.model.users

data class CreateFriendRequest(
    val userID: Long,
    val friendID: Long
)

data class CreateFriendResponse(
    val success: Boolean,
    val message: String?,
    val friendshipID: Long?
)

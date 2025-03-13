package io.winapps.voizy.data.model.users

data class Friend(
    val friendshipID: Long,
    val userID: Long,
    val status: String,
    val createdAt: String?,
    val friendUsername: String?,
    val firstName: String?,
    val lastName: String?,
    val preferredName: String,
    val profilePicURL: String?
)

data class ListFriendshipsResponse(
    val friends: List<Friend> = emptyList(),
    val limit: Long,
    val page: Long,
    val totalFriends: Long,
    val totalPages: Long
)
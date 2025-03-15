package io.winapps.voizy.data.model.posts

data class Comment(
    val commentID: Long,
    val postID: Long,
    val userID: Long,
    val contentText: String,
    val createdAt: String?,
    val updatedAt: String?,
    val username: String,
    val firstName: String?,
    val lastName: String?,
    val preferredName: String,
    val profilePicURL: String?,
    val reactions: List<String>,
    val reactionCount: Long
)

data class ListCommentsResponse(
    val comments: List<Comment> = emptyList(),
    val limit: Long,
    val page: Long,
    val totalComments: Long,
    val totalPages: Long
)
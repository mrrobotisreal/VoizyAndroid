package io.winapps.voizy.data.model.posts

data class PutPostCommentRequest(
    val postID: Long,
    val userID: Long,
    val contentText: String
)

data class PutPostCommentResponse(
    val success: Boolean,
    val message: String?,
    val commentID: Long?
)
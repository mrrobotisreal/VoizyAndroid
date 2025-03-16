package io.winapps.voizy.data.model.posts

data class UpdatePostRequest(
    val postID: Long,
    val userID: Long,
    val images: List<String>?
)

data class UpdatePostResponse(
    val success: Boolean,
    val message: String?,
    val postID: Long?
)
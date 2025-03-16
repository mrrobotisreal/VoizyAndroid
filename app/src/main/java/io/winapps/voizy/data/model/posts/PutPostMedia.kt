package io.winapps.voizy.data.model.posts

data class PutPostMediaRequest(
    val postID: Long,
    val images: List<String>
)

data class PutPostMediaResponse(
    val success: Boolean,
    val message: String?,
    val postID: Long?
)
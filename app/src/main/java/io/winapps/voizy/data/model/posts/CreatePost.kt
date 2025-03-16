package io.winapps.voizy.data.model.posts

data class CreatePostRequest(
    val userID: Long,
    val toUserID: Long,
    val originalPostID: Long?,
    val contentText: String,
    val locationName: String?,
    val locationLat: Double?,
    val locationLong: Double?,
    val images: List<String> = emptyList(),
    val hashtags: List<String> = emptyList(),
    val isPoll: Boolean,
    val pollQuestion: String?,
    val pollDurationType: String?,
    val pollDurationLength: Long?,
    val pollOptions: List<String> = emptyList()
)

data class CreatePostResponse(
    val success: Boolean,
    val message: String?,
    val postID: Long?
)
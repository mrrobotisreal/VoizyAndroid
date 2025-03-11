package io.winapps.voizy.data.model.posts

data class ListPost(
    val postID: Long,
    val userID: Long,
    val originalPostID: Long?,
    val contentText: String,
    val createdAt: String?,
    val updatedAt: String?,
    val locationName: String?,
    val locationLat: Double?,
    val locationLong: Double?,
    val isPoll: Boolean,
    val pollQuestion: String?,
    val pollDurationType: String?,
    val pollDurationLength: Long?
)

data class ListPostsResponse(
    val posts: List<ListPost>,
    val limit: Long,
    val page: Long,
    val totalPosts: Long,
    val totalPages: Long
)

data class CompletePost(
    val post: ListPost,
    val reactions: List<Reaction>? = emptyList(),
    val hashtags: List<String>? = emptyList(),
    val images: List<String>? = emptyList(),
    val videos: List<String>? = emptyList()
)

package io.winapps.voizy.data.model.posts

data class ListPost(
    val postID: Long,
    val userID: Long,
    val toUserID: Long,
    val originalPostID: Long?,
    val firstName: String?,
    val lastName: String?,
    val preferredName: String,
    val username: String,
    val impressions: Long,
    val views: Long,
    val contentText: String,
    val createdAt: String?,
    val updatedAt: String?,
    val locationName: String?,
    val locationLat: Double?,
    val locationLong: Double?,
    val isPoll: Boolean,
    val pollQuestion: String?,
    val pollDurationType: String?,
    val pollDurationLength: Long?,
    val userReaction: String?,
    val totalReactions: Long,
    val totalComments: Long,
    val totalPostShares: Long
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
    val profilePicURL: String?,
    val totalComments: Long?,
    val reactions: List<Reaction>? = emptyList(),
    val hashtags: List<String>? = emptyList(),
    val images: List<String>? = emptyList(),
    val videos: List<String>? = emptyList()
)

package io.winapps.voizy.data.model.posts

data class GetFriendFeedResponse(
    val friendPosts: List<ListPost>,
    val limit: Long,
    val page: Long
)

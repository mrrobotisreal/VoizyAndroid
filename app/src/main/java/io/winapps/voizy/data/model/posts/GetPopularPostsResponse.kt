package io.winapps.voizy.data.model.posts

data class GetPopularPostsResponse(
    val popularPosts: List<ListPost>,
    val limit: Long,
    val page: Long
)
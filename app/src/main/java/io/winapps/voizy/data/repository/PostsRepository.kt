package io.winapps.voizy.data.repository

import io.winapps.voizy.SessionViewModel
import io.winapps.voizy.data.model.posts.ListPostsResponse
import io.winapps.voizy.data.remote.posts.PostsApi
import io.winapps.voizy.data.remote.posts.PostsService
import javax.inject.Inject

class PostsRepository @Inject constructor(
) {
    private val service: PostsService = PostsApi.service

    suspend fun listPosts(apiKey: String, userIdHeader: String, userId: Long, limit: Long, page: Long): ListPostsResponse {
        return service.getPosts(
            apiKey = apiKey,
            userIdHeader = userIdHeader,
            userId = userId,
            limit = limit,
            page = page
        )
    }
}
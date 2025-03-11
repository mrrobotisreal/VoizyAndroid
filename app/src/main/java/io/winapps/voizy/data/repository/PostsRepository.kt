package io.winapps.voizy.data.repository

import io.winapps.voizy.SessionViewModel
import io.winapps.voizy.data.model.posts.GetPostDetailsResponse
import io.winapps.voizy.data.model.posts.GetPostMediaResponse
import io.winapps.voizy.data.model.posts.GetTotalPostsResponse
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

    suspend fun getTotalPosts(apiKey: String, userIdHeader: String, userId: Long): GetTotalPostsResponse {
        return service.getTotalPosts(
            apiKey = apiKey,
            userIdHeader = userIdHeader,
            userId = userId
        )
    }

    suspend fun getPostDetails(apiKey: String, userIdHeader: String, postId: Long): GetPostDetailsResponse {
        return service.getPostDetails(
            apiKey = apiKey,
            userIdHeader = userIdHeader,
            postId = postId
        )
    }

    suspend fun getPostMedia(apiKey: String, userIdHeader: String, postId: Long): GetPostMediaResponse {
        return service.getPostMedia(
            apiKey = apiKey,
            userIdHeader = userIdHeader,
            postId = postId
        )
    }
}
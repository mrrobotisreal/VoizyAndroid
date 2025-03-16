package io.winapps.voizy.data.repository

import io.winapps.voizy.data.model.posts.CreatePostRequest
import io.winapps.voizy.data.model.posts.CreatePostResponse
import io.winapps.voizy.data.model.posts.GetBatchPresignedPutUrlRequest
import io.winapps.voizy.data.model.posts.GetBatchPresignedPutUrlResponse
import io.winapps.voizy.data.model.posts.GetPostDetailsResponse
import io.winapps.voizy.data.model.posts.GetPostMediaResponse
import io.winapps.voizy.data.model.posts.GetTotalCommentsResponse
import io.winapps.voizy.data.model.posts.GetTotalPostsResponse
import io.winapps.voizy.data.model.posts.ListCommentsResponse
import io.winapps.voizy.data.model.posts.ListPostsResponse
import io.winapps.voizy.data.model.posts.PutPostCommentRequest
import io.winapps.voizy.data.model.posts.PutPostCommentResponse
import io.winapps.voizy.data.model.posts.PutPostMediaRequest
import io.winapps.voizy.data.model.posts.PutPostMediaResponse
import io.winapps.voizy.data.model.posts.PutPostReactionRequest
import io.winapps.voizy.data.model.posts.PutPostReactionResponse
import io.winapps.voizy.data.model.posts.UpdatePostRequest
import io.winapps.voizy.data.model.posts.UpdatePostResponse
import io.winapps.voizy.data.remote.posts.PostsApi
import io.winapps.voizy.data.remote.posts.PostsService
import javax.inject.Inject

class PostsRepository @Inject constructor(
) {
    private val service: PostsService = PostsApi.service

    suspend fun createPost(apiKey: String, userIdHeader: String, token: String, createPostRequest: CreatePostRequest): CreatePostResponse {
        return service.createPost(
            apiKey = apiKey,
            userIdHeader = userIdHeader,
            token = token,
            createPostRequest = createPostRequest,
        )
    }

    suspend fun getBatchPresignedPutUrls(apiKey: String, userIdHeader: String, token: String, getBatchPresignedPutUrlRequest: GetBatchPresignedPutUrlRequest): GetBatchPresignedPutUrlResponse {
        return service.getBatchPresignedPutUrls(
            apiKey = apiKey,
            userIdHeader = userIdHeader,
            token = token,
            getBatchPresignedPutUrlRequest = getBatchPresignedPutUrlRequest,
        )
    }

    suspend fun updatePost(apiKey: String, userIdHeader: String, token: String, updatePostRequest: UpdatePostRequest): UpdatePostResponse {
        return service.updatePost(
            apiKey = apiKey,
            userIdHeader = userIdHeader,
            token = token,
            updatePostRequest = updatePostRequest,
        )
    }

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

    suspend fun putPostMedia(apiKey: String, userIdHeader: String, token: String, putPostMediaRequest: PutPostMediaRequest): PutPostMediaResponse {
        return service.putPostMedia(
            apiKey = apiKey,
            userIdHeader = userIdHeader,
            token = token,
            putPostMediaRequest = putPostMediaRequest,
        )
    }

    suspend fun putPostReaction(apiKey: String, userIdHeader: String, token: String, putPostReactionRequest: PutPostReactionRequest): PutPostReactionResponse {
        return service.putPostReaction(
            apiKey = apiKey,
            userIdHeader = userIdHeader,
            token = token,
            putPostReactionRequest = putPostReactionRequest,
        )
    }

    suspend fun getTotalComments(apiKey: String, userIdHeader: String, postId: Long): GetTotalCommentsResponse {
        return service.getTotalComments(
            apiKey = apiKey,
            userIdHeader = userIdHeader,
            postId = postId
        )
    }

    suspend fun listComments(apiKey: String, userIdHeader: String, postId: Long, limit: Long, page: Long): ListCommentsResponse {
        return service.listComments(
            apiKey = apiKey,
            userIdHeader = userIdHeader,
            postId = postId,
            limit = limit,
            page = page,
        )
    }

    suspend fun putPostComment(apiKey: String, userIdHeader: String, token: String, putPostCommentRequest: PutPostCommentRequest): PutPostCommentResponse {
        return service.putPostComment(
            apiKey = apiKey,
            userIdHeader = userIdHeader,
            token = token,
            putPostCommentRequest = putPostCommentRequest,
        )
    }
}
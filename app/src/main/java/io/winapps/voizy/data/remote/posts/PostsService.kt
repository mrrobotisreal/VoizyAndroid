package io.winapps.voizy.data.remote.posts

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
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface PostsService {
    @POST("/posts/create")
    suspend fun createPost(
        @Header("X-API-Key") apiKey: String,
        @Header("X-User-ID") userIdHeader: String,
        @Header("Authorization") token: String,
        @Body createPostRequest: CreatePostRequest
    ): CreatePostResponse

    @POST("/posts/batch/get/presigned")
    suspend fun getBatchPresignedPutUrls(
        @Header("X-API-Key") apiKey: String,
        @Header("X-User-ID") userIdHeader: String,
        @Header("Authorization") token: String,
        @Body getBatchPresignedPutUrlRequest: GetBatchPresignedPutUrlRequest
    ): GetBatchPresignedPutUrlResponse

    @PUT("/posts/update")
    suspend fun updatePost(
        @Header("X-API-Key") apiKey: String,
        @Header("X-User-ID") userIdHeader: String,
        @Header("Authorization") token: String,
        @Body updatePostRequest: UpdatePostRequest
    ): UpdatePostResponse

    @GET("posts/list")
    suspend fun getPosts(
        @Header("X-API-Key") apiKey: String,
        @Header("X-User-ID") userIdHeader: String,
        @Query("id") userId: Long,
        @Query("limit") limit: Long,
        @Query("page") page: Long
    ): ListPostsResponse

    @GET("posts/get/total")
    suspend fun getTotalPosts(
        @Header("X-API-Key") apiKey: String,
        @Header("X-User-ID") userIdHeader: String,
        @Query("id") userId: Long
    ): GetTotalPostsResponse

    @GET("posts/get/details")
    suspend fun getPostDetails(
        @Header("X-API-Key") apiKey: String,
        @Header("X-User-ID") userIdHeader: String,
        @Query("id") postId: Long
    ): GetPostDetailsResponse

    @GET("posts/get/media")
    suspend fun getPostMedia(
        @Header("X-API-Key") apiKey: String,
        @Header("X-User-ID") userIdHeader: String,
        @Query("id") postId: Long
    ): GetPostMediaResponse

    @PUT("posts/put/media")
    suspend fun putPostMedia(
        @Header("X-API-Key") apiKey: String,
        @Header("X-User-ID") userIdHeader: String,
        @Header("Authorization") token: String,
        @Body putPostMediaRequest: PutPostMediaRequest,
    ): PutPostMediaResponse

    @PUT("/posts/reactions/put")
    suspend fun putPostReaction(
        @Header("X-API-Key") apiKey: String,
        @Header("X-User-ID") userIdHeader: String,
        @Header("Authorization") token: String,
        @Body putPostReactionRequest: PutPostReactionRequest
    ): PutPostReactionResponse

    @GET("/posts/comments/get/total")
    suspend fun getTotalComments(
        @Header("X-API-Key") apiKey: String,
        @Header("X-User-ID") userIdHeader: String,
        @Query("id") postId: Long
    ): GetTotalCommentsResponse

    @GET("/posts/comments/list")
    suspend fun listComments(
        @Header("X-API-Key") apiKey: String,
        @Header("X-User-ID") userIdHeader: String,
        @Query("id") postId: Long,
        @Query("limit") limit: Long,
        @Query("page") page: Long
    ): ListCommentsResponse

    @PUT("/posts/comments/put")
    suspend fun putPostComment(
        @Header("X-API-Key") apiKey: String,
        @Header("X-User-ID") userIdHeader: String,
        @Header("Authorization") token: String,
        @Body putPostCommentRequest: PutPostCommentRequest
    ): PutPostCommentResponse
}
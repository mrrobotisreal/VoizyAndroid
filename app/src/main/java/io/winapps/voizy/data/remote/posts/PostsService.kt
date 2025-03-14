package io.winapps.voizy.data.remote.posts

import io.winapps.voizy.data.model.posts.GetPostDetailsResponse
import io.winapps.voizy.data.model.posts.GetPostMediaResponse
import io.winapps.voizy.data.model.posts.GetTotalPostsResponse
import io.winapps.voizy.data.model.posts.ListPostsResponse
import io.winapps.voizy.data.model.posts.PutPostReactionRequest
import io.winapps.voizy.data.model.posts.PutPostReactionResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PUT
import retrofit2.http.Query

interface PostsService {
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

    @PUT("/posts/reactions/put")
    suspend fun putPostReaction(
        @Header("X-API-Key") apiKey: String,
        @Header("X-User-ID") userIdHeader: String,
        @Header("Authorization") token: String,
        @Body putPostReactionRequest: PutPostReactionRequest
    ): PutPostReactionResponse
}
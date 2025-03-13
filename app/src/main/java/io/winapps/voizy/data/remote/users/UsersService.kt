package io.winapps.voizy.data.remote.users

import io.winapps.voizy.data.model.users.CreateAccountRequest
import io.winapps.voizy.data.model.users.CreateAccountResponse
import io.winapps.voizy.data.model.users.GetProfilePicResponse
import io.winapps.voizy.data.model.users.GetTotalFriendsResponse
import io.winapps.voizy.data.model.users.GetTotalUserImagesResponse
import io.winapps.voizy.data.model.users.ListFriendshipsResponse
import io.winapps.voizy.data.model.users.ListUserImagesResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface UsersService {
    @POST("/users/create")
    suspend fun createAccount(@Body createAccountRequest: CreateAccountRequest): CreateAccountResponse

    @GET("/users/friends/get/total")
    suspend fun getTotalFriends(
        @Header("X-API-Key") apiKey: String,
        @Header("X-User-ID") userIdHeader: String,
        @Query("id") userId: Long,
    ): GetTotalFriendsResponse

    @GET("/users/friends/list")
    suspend fun listFriendships(
        @Header("X-API-Key") apiKey: String,
        @Header("X-User-ID") userIdHeader: String,
        @Query("id") userId: Long,
        @Query("limit") limit: Long,
        @Query("page") page: Long,
    ): ListFriendshipsResponse

    @GET("/users/images/get/total")
    suspend fun getTotalImages(
        @Header("X-API-Key") apiKey: String,
        @Header("X-User-ID") userIdHeader: String,
        @Query("id") userId: Long,
    ): GetTotalUserImagesResponse

    @GET("/users/images/get/profilePic")
    suspend fun getProfilePic(
        @Header("X-API-Key") apiKey: String,
        @Header("X-User-ID") userIdHeader: String,
        @Query("id") userId: Long,
    ): GetProfilePicResponse

    @GET("/users/images/list")
    suspend fun listImages(
        @Header("X-API-Key") apiKey: String,
        @Header("X-User-ID") userIdHeader: String,
        @Query("id") userId: Long,
        @Query("limit") limit: Long,
        @Query("page") page: Long,
    ): ListUserImagesResponse
}
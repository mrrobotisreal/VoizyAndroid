package io.winapps.voizy.data.remote.users

import io.winapps.voizy.data.model.users.CreateAccountRequest
import io.winapps.voizy.data.model.users.CreateAccountResponse
import io.winapps.voizy.data.model.users.CreateFriendRequest
import io.winapps.voizy.data.model.users.CreateFriendResponse
import io.winapps.voizy.data.model.users.GetBatchUserImagesPresignedPutUrlsRequest
import io.winapps.voizy.data.model.users.GetBatchUserImagesPresignedPutUrlsResponse
import io.winapps.voizy.data.model.users.GetCoverPicResponse
import io.winapps.voizy.data.model.users.GetFriendStatusResponse
import io.winapps.voizy.data.model.users.GetProfilePicResponse
import io.winapps.voizy.data.model.users.GetProfileResponse
import io.winapps.voizy.data.model.users.GetTotalFriendsResponse
import io.winapps.voizy.data.model.users.GetTotalUserImagesResponse
import io.winapps.voizy.data.model.users.ListFriendshipsResponse
import io.winapps.voizy.data.model.users.ListPeopleYouMayKnowResponse
import io.winapps.voizy.data.model.users.ListUserImagesResponse
import io.winapps.voizy.data.model.users.PutUserImagesRequest
import io.winapps.voizy.data.model.users.PutUserImagesResponse
import io.winapps.voizy.data.model.users.UpdateCoverPicRequest
import io.winapps.voizy.data.model.users.UpdateCoverPicResponse
import io.winapps.voizy.data.model.users.UpdateProfilePicRequest
import io.winapps.voizy.data.model.users.UpdateProfilePicResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface UsersService {
    @POST("/users/create")
    suspend fun createAccount(@Body createAccountRequest: CreateAccountRequest): CreateAccountResponse

    @GET("/users/profile/get")
    suspend fun getProfileInfo(
        @Header("X-API-Key") apiKey: String,
        @Header("X-User-ID") userIdHeader: String,
        @Query("id") userId: Long,
    ): GetProfileResponse

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

    @GET("/users/images/get/coverPic")
    suspend fun getCoverPic(
        @Header("X-API-Key") apiKey: String,
        @Header("X-User-ID") userIdHeader: String,
        @Query("id") userId: Long,
    ): GetCoverPicResponse

    @GET("/users/images/list")
    suspend fun listImages(
        @Header("X-API-Key") apiKey: String,
        @Header("X-User-ID") userIdHeader: String,
        @Query("id") userId: Long,
        @Query("limit") limit: Long,
        @Query("page") page: Long,
    ): ListUserImagesResponse

    @POST("/users/images/batch/get/presigned")
    suspend fun getBatchUserImagesPresignedUrls(
        @Header("X-API-Key") apiKey: String,
        @Header("X-User-ID") userIdHeader: String,
        @Header("Authorization") token: String,
        @Body getBatchUserImagesPresignedPutUrlsRequest: GetBatchUserImagesPresignedPutUrlsRequest
    ): GetBatchUserImagesPresignedPutUrlsResponse

    @PUT("/users/images/put")
    suspend fun putUserImages(
        @Header("X-API-Key") apiKey: String,
        @Header("X-User-ID") userIdHeader: String,
        @Header("Authorization") token: String,
        @Body putUserImagesRequest: PutUserImagesRequest
    ): PutUserImagesResponse

    @PUT("/users/images/profilePic/update")
    suspend fun updateProfilePic(
        @Header("X-API-Key") apiKey: String,
        @Header("X-User-ID") userIdHeader: String,
        @Header("Authorization") token: String,
        @Body updateProfilePicRequest: UpdateProfilePicRequest
    ): UpdateProfilePicResponse

    @PUT("/users/images/coverPic/update")
    suspend fun updateCoverPic(
        @Header("X-API-Key") apiKey: String,
        @Header("X-User-ID") userIdHeader: String,
        @Header("Authorization") token: String,
        @Body updateCoverPicRequest: UpdateCoverPicRequest
    ): UpdateCoverPicResponse

    @GET("/users/friends/people/list")
    suspend fun listPeopleYouMayKnow(
        @Header("X-API-Key") apiKey: String,
        @Header("X-User-ID") userIdHeader: String,
        @Query("id") userId: Long,
        @Query("limit") limit: Long,
        @Query("page") page: Long
    ): ListPeopleYouMayKnowResponse

    @GET("/users/friends/get/status")
    suspend fun getFriendStatus(
        @Header("X-API-Key") apiKey: String,
        @Header("X-User-ID") userIdHeader: String,
        @Query("id") userId: Long,
        @Query("friend") friendId: Long
    ): GetFriendStatusResponse

    @POST("/users/friends/create")
    suspend fun createFriendship(
        @Header("X-API-Key") apiKey: String,
        @Header("X-User-ID") userIdHeader: String,
        @Header("Authorization") token: String,
        @Body createFriendRequest: CreateFriendRequest
    ): CreateFriendResponse
}
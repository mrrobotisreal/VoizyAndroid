package io.winapps.voizy.data.remote.users

import io.winapps.voizy.data.model.users.CreateAccountRequest
import io.winapps.voizy.data.model.users.CreateAccountResponse
import io.winapps.voizy.data.model.users.GetTotalFriendsResponse
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
}
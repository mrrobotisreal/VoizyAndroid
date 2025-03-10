package io.winapps.voizy.data.remote.users

import io.winapps.voizy.data.model.users.CreateAccountRequest
import io.winapps.voizy.data.model.users.CreateAccountResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface UsersService {
    @POST("/users/create")
    suspend fun createAccount(@Body createAccountRequest: CreateAccountRequest): CreateAccountResponse
}
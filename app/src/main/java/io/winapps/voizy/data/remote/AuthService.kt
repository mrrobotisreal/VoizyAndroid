package io.winapps.voizy.data.remote

import io.winapps.voizy.data.model.auth.LoginRequest
import io.winapps.voizy.data.model.auth.LoginResponse
import io.winapps.voizy.data.model.users.CreateAccountRequest
import io.winapps.voizy.data.model.users.CreateAccountResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("/users/login")
    suspend fun login(@Body loginRequest: LoginRequest): LoginResponse

    @POST("/users/create")
    suspend fun createAccount(@Body createAccountRequest: CreateAccountRequest): CreateAccountResponse
}
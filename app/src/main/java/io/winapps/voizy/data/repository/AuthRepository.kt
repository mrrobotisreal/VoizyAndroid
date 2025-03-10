package io.winapps.voizy.data.repository

import io.winapps.voizy.data.local.SecureStorage
import io.winapps.voizy.data.model.auth.LoginRequest
import io.winapps.voizy.data.model.auth.LoginResponse
import io.winapps.voizy.data.model.users.CreateAccountRequest
import io.winapps.voizy.data.model.users.CreateAccountResponse
import io.winapps.voizy.data.remote.AuthApi

class AuthRepository(private val secureStorage: SecureStorage) {
    private val authService = AuthApi.service

    suspend fun login(username: String, password: String): LoginResponse {
        val response = authService.login(LoginRequest(username, password))

        secureStorage.saveApiKey(response.apiKey)
        secureStorage.saveToken(response.token)

        return response
    }

    suspend fun createAccount(email: String, username: String, preferredName: String, password: String): CreateAccountResponse {
        val response = authService.createAccount(CreateAccountRequest(email, username, preferredName, password))

        secureStorage.saveApiKey(response.apiKey)
        secureStorage.saveToken(response.token)

        return response
    }
}
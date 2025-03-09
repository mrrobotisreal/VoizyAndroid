package io.winapps.voizy.data.repository

import io.winapps.voizy.data.local.SecureStorage
import io.winapps.voizy.data.model.auth.LoginRequest
import io.winapps.voizy.data.model.auth.LoginResponse
import io.winapps.voizy.data.remote.AuthApi

class AuthRepository(private val secureStorage: SecureStorage) {
    private val authService = AuthApi.service

    suspend fun login(username: String, password: String): LoginResponse {
        val response = authService.login(LoginRequest(username, password))

        secureStorage.saveApiKey(response.apiKey)
        secureStorage.saveToken(response.token)

        return response
    }
}
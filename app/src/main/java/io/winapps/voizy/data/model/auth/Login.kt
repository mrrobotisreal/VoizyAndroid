package io.winapps.voizy.data.model.auth

data class LoginRequest(
    val username: String,
    val password: String
)

data class LoginResponse(
    val userID: String,
    val username: String,
    val email: String,
    val apiKey: String,
    val token: String
)
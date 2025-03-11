package io.winapps.voizy.data.model.users

import java.util.Date

data class CreateAccountRequest(
    val email: String,
    val username: String,
    val preferredName: String,
    val password: String
)

data class CreateAccountResponse(
    val userID: Long,
    val profileID: Long,
    val apiKey: String,
    val token: String,
    val email: String,
    val username: String,
    val preferredName: String,
    val firstName: String,
    val lastName: String,
    val birthDate: String?,
    val cityOfResidence: String,
    val placeOfWork: String,
    val dateJoined: String?,
    val createdAt: String?,
    val updatedAt: String?
)
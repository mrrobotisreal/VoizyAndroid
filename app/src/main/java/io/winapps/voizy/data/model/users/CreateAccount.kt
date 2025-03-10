package io.winapps.voizy.data.model.users

import java.util.Date

data class CreateAccountRequest(
    val email: String,
    val username: String,
    val preferredName: String,
    val password: String
)

data class CreateAccountResponse(
    val userID: Int,
    val profileID: Int,
    val apiKey: String,
    val token: String,
    val email: String,
    val username: String,
    val preferredName: String,
    val firstName: String,
    val lastName: String,
    val birthDate: Date,
    val cityOfResidence: String,
    val placeOfWork: String,
    val dateJoined: Date,
    val createdAt: Date,
    val updatedAt: Date
)
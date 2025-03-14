package io.winapps.voizy.data.model.users

data class GetProfileResponse(
    val userID: Long,
    val profileID: Long,
    val firstName: String?,
    val lastName: String?,
    val preferredName: String,
    val birthDate: String?,
    val cityOfResidence: String?,
    val placeOfWork: String?,
    val dateJoined: String?,
    val username: String
)
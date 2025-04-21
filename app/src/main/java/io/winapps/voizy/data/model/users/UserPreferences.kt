package io.winapps.voizy.data.model.users

data class GetUserPreferencesResponse(
    val userID: Long,
    val primaryColor: String,
    val primaryAccent: String,
    val secondaryColor: String,
    val secondaryAccent: String,
    val songAutoplay: Boolean,
    val profilePrimaryColor: String,
    val profilePrimaryAccent: String,
    val profileSecondaryColor: String,
    val profileSecondaryAccent: String,
    val profileSongAutoplay: Boolean
)

data class PutUserPreferencesRequest(
    val userID: Long,
    val primaryColor: String?,
    val primaryAccent: String?,
    val secondaryColor: String?,
    val secondaryAccent: String?,
    val songAutoplay: Boolean?,
    val profilePrimaryColor: String?,
    val profilePrimaryAccent: String?,
    val profileSecondaryColor: String?,
    val profileSecondaryAccent: String?,
    val profileSongAutoplay: Boolean?
)

data class PutUserPreferencesResponse(
    val success: Boolean,
    val message: String?,
    val userPreferencesID: Long?
)

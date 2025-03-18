package io.winapps.voizy.data.model.users

data class UserImage(
    val userID: Long,
    val imageID: Long,
    val imageURL: String,
    val isProfilePic: Boolean,
    val isCoverPic: Boolean,
    val uploadedAt: String?
)

data class GetTotalUserImagesResponse(
    val totalImages: Long
)

data class ListUserImagesResponse(
    val images: List<UserImage>,
    val limit: Long,
    val page: Long,
    val totalImages: Long,
    val totalPages: Long
)

data class UpdateProfilePicRequest(
    val userID: Long,
    val imageID: Long
)

data class UpdateProfilePicResponse(
    val success: Boolean,
    val message: String?
)

data class UpdateCoverPicRequest(
    val userID: Long,
    val imageID: Long
)

data class UpdateCoverPicResponse(
    val success: Boolean,
    val message: String?
)
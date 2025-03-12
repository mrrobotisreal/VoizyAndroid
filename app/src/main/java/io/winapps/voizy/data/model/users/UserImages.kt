package io.winapps.voizy.data.model.users

data class UserImage(
    val userId: Long,
    val imageUrl: String
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
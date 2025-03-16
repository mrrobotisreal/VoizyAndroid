package io.winapps.voizy.data.model.users

data class PutUserImagesRequest(
    val userID: Long,
    val images: List<String>
)

data class PutUserImagesResponse(
    val success: Boolean,
    val message: String?
)
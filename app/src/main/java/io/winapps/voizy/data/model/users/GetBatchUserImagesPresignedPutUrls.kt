package io.winapps.voizy.data.model.users

data class GetBatchUserImagesPresignedPutUrlsRequest(
    val userID: Long,
    val fileNames: List<String>
)

data class PresignedUserImageFile(
    val fileName: String,
    val presignedURL: String,
    val finalURL: String
)

data class GetBatchUserImagesPresignedPutUrlsResponse(
    val images: List<PresignedUserImageFile>
)
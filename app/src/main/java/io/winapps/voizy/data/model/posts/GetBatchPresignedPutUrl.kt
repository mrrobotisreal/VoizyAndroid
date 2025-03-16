package io.winapps.voizy.data.model.posts

data class GetBatchPresignedPutUrlRequest(
    val postID: Long,
    val userID: Long,
    val fileNames: List<String>
)

data class PresignedFile(
    val fileName: String,
    val presignedURL: String,
    val finalURL: String,
)

data class GetBatchPresignedPutUrlResponse(
    val images: List<PresignedFile> = emptyList()
)
package io.winapps.voizy.data.model.posts

enum class ReactionType(val label: String) {
    LIKE("like"),
    LOVE("love"),
    LAUGH("laugh"),
    CONGRATULATE("congratulate"),
    SHOCKED("shocked"),
    SAD("sad"),
    ANGRY("angry"),
}

data class PutPostReactionRequest(
    val postID: Long,
    val userID: Long,
    val reactionType: String
)

data class PutPostReactionResponse(
    val success: Boolean,
    val message: String?,
    val reactionID: Long?
)
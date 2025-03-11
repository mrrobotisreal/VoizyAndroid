package io.winapps.voizy.data.model.posts

data class Reaction(
    val reactionID: Long,
    val postID: Long,
    val userID: Long,
    val reactionType: String,
    val reactedAt: String?
)

data class GetPostDetailsResponse(
    val reactions: List<Reaction>?,
    val hashtags: List<String>?
)
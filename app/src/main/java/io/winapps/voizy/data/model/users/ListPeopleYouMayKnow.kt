package io.winapps.voizy.data.model.users

data class Person(
    val userID: Long,
    val username: String,
    val firstName: String?,
    val lastName: String?,
    val preferredName: String,
    val profilePicURL: String?,
    val cityOfResidence: String?,
    val friendsInCommon: List<Long> = emptyList()
)

data class ListPeopleYouMayKnowResponse(
    val peopleYouMayKnow: List<Person>,
    val limit: Long,
    val page: Long
)

data class FriendInCommon(
    val userID: Long,
    val displayName: String,
    val profilePicURL: String?
)

data class CompletePerson(
    val userID: Long,
    val username: String,
    val displayName: String,
    val profilePicURL: String?,
    val cityOfResidence: String?,
    val friendsInCommon: List<FriendInCommon> = emptyList()
)
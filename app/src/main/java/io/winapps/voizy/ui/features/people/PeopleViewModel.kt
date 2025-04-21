package io.winapps.voizy.ui.features.people

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.winapps.voizy.data.model.users.CompletePerson
import io.winapps.voizy.data.model.users.FriendInCommon
import io.winapps.voizy.data.model.users.Person
import io.winapps.voizy.data.repository.UsersRepository
import io.winapps.voizy.util.GetDisplayName
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PeopleViewModel @Inject constructor(
    private val usersRepository: UsersRepository
) : ViewModel() {
    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var peopleYouMayKnow by mutableStateOf(emptyList<CompletePerson>())
        private set

    fun loadPeopleYouMayKnow(userId: Long, apiKey: String, limit: Long = 30, page: Long = 1, forceRefresh: Boolean = false) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            try {
                val response = usersRepository.listPeopleYouMayKnow(
                    apiKey = apiKey,
                    userIdHeader = userId.toString(),
                    userId = userId,
                    limit = limit,
                    page = page
                )
                val rawPeople = response.peopleYouMayKnow

                val finalList = mutableListOf<CompletePerson>()
                for (person in rawPeople) {
                    val friendsInCommon = person.friendsInCommon
                    val friends = mutableListOf<FriendInCommon>()
                    for (friendID in friendsInCommon) {
                        val profilePicDeferred = async {
                            usersRepository.getProfilePic(
                                apiKey = apiKey,
                                userIdHeader = userId.toString(),
                                userId = friendID
                            )
                        }
                        val profileInfoDeferred = async {
                            usersRepository.getProfileInfo(
                                apiKey = apiKey,
                                userIdHeader = userId.toString(),
                                userId = friendID
                            )
                        }
                        val profilePicResponse = profilePicDeferred.await()
                        val profileInfoResponse = profileInfoDeferred.await()
                        val friendDisplayName = GetDisplayName(
                            username = profileInfoResponse.username,
                            preferredName = profileInfoResponse.preferredName,
                            firstName = profileInfoResponse.firstName,
                            lastName = profileInfoResponse.lastName
                        )

                        val friend = FriendInCommon(
                            userID = friendID,
                            displayName = friendDisplayName,
                            profilePicURL = profilePicResponse.profilePicURL
                        )
                        friends.add(friend)
                    }

                    val completePerson = CompletePerson(
                        userID = person.userID,
                        username = person.username,
                        displayName = GetDisplayName(
                            username = person.username,
                            preferredName = person.preferredName,
                            firstName = person.firstName,
                            lastName = person.lastName
                        ),
                        profilePicURL = person.profilePicURL,
                        cityOfResidence = person.cityOfResidence,
                        friendsInCommon = friends
                    )
                    finalList.add(completePerson)
                }

                peopleYouMayKnow = finalList
            } catch (e: Exception) {
                errorMessage = e.message
            } finally {
                isLoading = false
            }
        }
    }

    var searchResults by mutableStateOf(emptyList<Person>())
        private set

    fun onSearchPeople(query: String) {
        // TODO: implement this
    }

    var searchText by mutableStateOf("")
        private set

    fun onSearchTextChanged(newValue: String) {
        searchText = newValue
    }
}
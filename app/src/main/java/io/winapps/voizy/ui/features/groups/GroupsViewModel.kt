package io.winapps.voizy.ui.features.groups

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.winapps.voizy.data.model.users.Group
import io.winapps.voizy.data.model.users.GroupPrivacy
import javax.inject.Inject

enum class GroupTopic(val label: String) {
    ALL("All"),
    SCIENCE_AND_TECH("Science & tech"),
    TRAVEL("Travel"),
    BUSINESS("Business"),
    EDUCATION("Education"),
    SPORTS_AND_FITNESS("Sports & fitness"),
    ART("Art"),
    CULTURE("Culture"),
    HOBBIES_AND_INTERESTS("Hobbies & interests"),
    RELATIONSHIPS("Relationships"),
    FOOD_AND_DRINK("Food & drink"),
    CONSPIRACY_THEORIES("Conspiracy theories"),
    POLITICS("Politics"),
    HISTORY("History");

    companion object {
        val default = ALL
    }
}

@HiltViewModel
class GroupsViewModel @Inject constructor() : ViewModel() {
    var searchText by mutableStateOf("")
        private set

    var selectedTopic by mutableStateOf<GroupTopic>(GroupTopic.ALL)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var groups by mutableStateOf<List<Group>>(emptyList())
        private set

//    var groups by mutableStateOf<List<Group>>(listOf(
//        Group(
//            groupID = 1,
//            name = "Software Engineers UNITE!",
//            description = "",
//            privacy = GroupPrivacy.PUBLIC,
//            creatorID = 1,
//            createdAt = "",
//            totalUsers = 497,
//            imageURL = "https://winapps-solutions-llc.s3.us-west-2.amazonaws.com/portfolio/voizy/mocks/SoftwareEngineersUnite.png"
//        ),
//        Group(
//            groupID = 2,
//            name = "Dogs are the best 🐶",
//            description = "",
//            privacy = GroupPrivacy.PUBLIC,
//            creatorID = 1,
//            createdAt = "",
//            totalUsers = 26543,
//            imageURL = "https://winapps-solutions-llc.s3.us-west-2.amazonaws.com/portfolio/voizy/mocks/DogsAreTheBest.jpg"
//        ),
//        Group(
//            groupID = 3,
//            name = "Kool Kid Krew 😎🍭",
//            description = "",
//            privacy = GroupPrivacy.PUBLIC,
//            creatorID = 1,
//            createdAt = "",
//            totalUsers = 12,
//            imageURL = "https://winapps-solutions-llc.s3.us-west-2.amazonaws.com/portfolio/voizy/mocks/KoolKidKrew.jpg"
//        )
//    ))
//        private set

    fun onSearchTextChanged(newValue: String) {
        searchText = newValue
    }

    fun onSelectTopic(newTopic: GroupTopic) {
        selectedTopic = newTopic
    }
}
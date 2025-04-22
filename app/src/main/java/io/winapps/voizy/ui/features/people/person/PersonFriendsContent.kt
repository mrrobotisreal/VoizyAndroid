package io.winapps.voizy.ui.features.people.person

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.winapps.voizy.AppScreen
import io.winapps.voizy.SessionViewModel
import io.winapps.voizy.ui.features.profile.FriendRow
import io.winapps.voizy.ui.theme.Ubuntu

@Composable
fun PersonFriendsContent(
    secondaryColor: Color,
    secondaryAccent: Color
) {
    val sessionViewModel = hiltViewModel<SessionViewModel>()
    val apiKey = sessionViewModel.getApiKey().orEmpty()
    val userId = sessionViewModel.userId ?: -1
    val personViewModel = hiltViewModel<PersonViewModel>()

    LaunchedEffect(Unit) {
        personViewModel.loadFriends(
            personId = personViewModel.selectedPersonId,
            userId = userId,
            apiKey = apiKey,
            limit = 50,
            page = 1,
        )
    }

    val filteredFriends = remember(personViewModel.friends, personViewModel.searchFriendsText) {
        if (personViewModel.searchFriendsText.isEmpty()) {
            personViewModel.friends
        } else {
            personViewModel.friends.filter { friend ->
                (friend.firstName?.contains(personViewModel.searchFriendsText, ignoreCase = true) == true) ||
                        (friend.lastName?.contains(personViewModel.searchFriendsText, ignoreCase = true) == true) ||
                        friend.preferredName.contains(personViewModel.searchFriendsText, ignoreCase = true) ||
                        (friend.friendUsername?.contains(personViewModel.searchFriendsText, ignoreCase = true) == true)
            }
        }
    }

    Column {
        OutlinedTextField(
            value = personViewModel.searchFriendsText,
            onValueChange = { personViewModel.onChangeSearchFriendsText(it) },
            label = { Text("Search friends", fontFamily = Ubuntu, fontWeight = FontWeight.Normal) },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                focusedTextColor = Color.Black,
                focusedLabelColor = Color.DarkGray,
                unfocusedContainerColor = Color.White,
                unfocusedTextColor = Color.Black,
                unfocusedLabelColor = Color.DarkGray
            ),
            trailingIcon = {
                IconButton(
                    onClick = {}
                ) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Search",
                        tint = secondaryColor
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(4.dp))

        LazyColumn {
            items(filteredFriends) { friend ->
                FriendRow(
                    friend = friend,
                    onClick = { friendToView ->
                        personViewModel.selectPerson(
                            personId = friendToView.userID,
                            username = friendToView.friendUsername ?: "",
                            userId = userId,
                            apiKey = apiKey
                            )
                        sessionViewModel.switchCurrentAppScreen(AppScreen.PERSON)
                    },
                    secondaryColor = secondaryColor,
                    secondaryAccent = secondaryAccent
                )
            }
        }
    }
}
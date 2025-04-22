package io.winapps.voizy.ui.features.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import io.winapps.voizy.SessionViewModel
import io.winapps.voizy.data.model.users.Friend
import io.winapps.voizy.ui.theme.Ubuntu

@Composable
fun FriendsContent(
    secondaryColor: Color,
    secondaryAccent: Color
) {
    val friendsViewModel = hiltViewModel<FriendsViewModel>()
    val sessionViewModel = hiltViewModel<SessionViewModel>()
    val friends = friendsViewModel.friends

    LaunchedEffect(Unit) {
        val userId = sessionViewModel.userId ?: 0
        val apiKey = sessionViewModel.getApiKey().orEmpty()
        friendsViewModel.loadFriends(
            userId = userId,
            apiKey = apiKey,
            limit = 50,
            page = 1,
        )
    }

    val filteredFriends = remember(friends, friendsViewModel.searchText) {
        if (friendsViewModel.searchText.isEmpty()) {
            friends
        } else {
            friends.filter { friend ->
                (friend.firstName?.contains(friendsViewModel.searchText, ignoreCase = true) == true) ||
                        (friend.lastName?.contains(friendsViewModel.searchText, ignoreCase = true) == true) ||
                        friend.preferredName.contains(friendsViewModel.searchText, ignoreCase = true) ||
                        (friend.friendUsername?.contains(friendsViewModel.searchText, ignoreCase = true) == true)
            }
        }
    }

    Column {
        OutlinedTextField(
            value = friendsViewModel.searchText,
            onValueChange = { friendsViewModel.onChangeSearchText(it) },
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
                    onClick = {},
                    secondaryColor = secondaryColor,
                    secondaryAccent = secondaryAccent
                )
            }
        }
    }
}

@Composable
fun FriendRow(
    friend: Friend,
    onClick: () -> Unit,
    secondaryColor: Color,
    secondaryAccent: Color
) {
    val displayName = if (!friend.firstName.isNullOrBlank() && !friend.lastName.isNullOrBlank()) {
        "${friend.firstName} ${friend.lastName}"
    } else {
        friend.preferredName
    }

    val displayUsername = "${friend.friendUsername}"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .padding(4.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(3.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(secondaryAccent)
                    .border(2.dp, secondaryColor, CircleShape)
            ) {
                if (!friend.profilePicURL.isNullOrEmpty()) {
                    val painter = rememberAsyncImagePainter(
                        model = friend.profilePicURL
                    )
                    Image(
                        painter = painter,
                        contentDescription = "User image",
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .border(2.dp, secondaryColor, CircleShape),
                        contentScale = ContentScale.Crop
                    )

                    if (painter.state is AsyncImagePainter.State.Loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center),
                            color = secondaryColor
                        )
                    }
//                AsyncImage(
//                    model = friend.profilePicURL,
//                    contentDescription = "Friend profile pic",
//                    modifier = Modifier.clip(CircleShape),
//                    contentScale = ContentScale.Crop
//                )
                } else {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Empty friend profile pic",
                        modifier = Modifier
                            .clip(CircleShape)
                            .align(Alignment.Center),
                        tint = secondaryColor
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column {
                Text(
                    text = displayName,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontFamily = Ubuntu,
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color.Black
                )
                Text(
                    text = displayUsername,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontFamily = Ubuntu,
                        fontWeight = FontWeight.Normal
                    ),
                    color = Color.DarkGray
                )
            }
        }
    }
}
package io.winapps.voizy.ui.features.profile

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import io.winapps.voizy.SessionViewModel
import io.winapps.voizy.data.model.users.Friend
import io.winapps.voizy.ui.theme.Ubuntu

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FriendsContent() {
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

    LazyColumn {
        items(friends) { friend ->
            FriendRow(
                friend = friend,
                onClick = {}
            )
        }
    }
}

@Composable
fun FriendRow(
    friend: Friend,
    onClick: () -> Unit
) {
    val displayName = if (!friend.firstName.isNullOrBlank() && !friend.lastName.isNullOrBlank()) {
        "${friend.firstName} ${friend.lastName}"
    } else {
        friend.preferredName
    }

    val displayUsername = "${friend.friendUsername}"

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
                .background(Color.Gray)
        ) {
            if (!friend.profilePicURL.isNullOrEmpty()) {
                AsyncImage(
                    model = friend.profilePicURL,
                    contentDescription = "Friend profile pic",
                    modifier = Modifier.clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Empty friend profile pic",
                    modifier = Modifier.clip(CircleShape).align(Alignment.Center)
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
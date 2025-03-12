package io.winapps.voizy.ui.features.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.winapps.voizy.R
import io.winapps.voizy.SessionViewModel
import io.winapps.voizy.ui.navigation.BottomNavBar
import io.winapps.voizy.ui.theme.Ubuntu

@Composable
fun ProfileScreen() {
    val postsViewModel = hiltViewModel<PostsViewModel>()
    val friendsViewModel = hiltViewModel<FriendsViewModel>()
    val sessionViewModel = hiltViewModel<SessionViewModel>()
    var selectedTab by remember { mutableStateOf(ProfileTab.POSTS) }

    // Sample stats for now
    val photoCount = 57

    LaunchedEffect(Unit) {
        val userId = sessionViewModel.userId ?: -1
        val apiKey = sessionViewModel.getApiKey().orEmpty()
        friendsViewModel.loadTotalFriends(
            userId = userId,
            apiKey = apiKey
        )
        postsViewModel.loadTotalPosts(
            userId = userId,
            apiKey = apiKey
        )
    }

    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xFFFDF4C9)),
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().height(200.dp),
        ) {
            Image(
                painter = painterResource(id = R.drawable.default_cover2),
                contentDescription = "Cover photo",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .offset(x = 16.dp, y = 40.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.test_profile_photo),
                    contentDescription = "Profile photo",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color(0xFFF10E91), CircleShape),
                    contentScale = ContentScale.Crop
                )
            }

            Button(
                onClick = { /* TODO: edit profile */ },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = (-16).dp, y = 40.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF10E91))
            ) {
                Text(
                    text = "Edit profile",
                    fontFamily = Ubuntu,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFFD5ED)
                )
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null,
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = "${friendsViewModel.totalFriends} Friends",
                    fontFamily = Ubuntu,
                    fontWeight = FontWeight.Normal
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                if (postsViewModel.isLoadingTotalPosts) {
                    CircularProgressIndicator(
                        color = Color(0xFFF10E91)
                    )
                } else {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.List,
                        contentDescription = null
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = "${postsViewModel.totalPosts} Posts",
                        fontFamily = Ubuntu,
                        fontWeight = FontWeight.Normal
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.AccountBox,
                    contentDescription = null
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = "$photoCount Photos",
                    fontFamily = Ubuntu,
                    fontWeight = FontWeight.Normal
                )
            }
        }

        ProfileTabsRow(
            selectedTab = selectedTab,
            onTabSelected = { selectedTab = it }
        )

        Box(
            modifier = Modifier.weight(1f)
        ) {
            when (selectedTab) {
                ProfileTab.POSTS -> PostsContent()
                ProfileTab.PHOTOS -> PhotosContent()
                ProfileTab.ABOUT -> AboutContent()
                ProfileTab.FRIENDS -> FriendsContent()
            }
        }

        BottomNavBar()
    }
}

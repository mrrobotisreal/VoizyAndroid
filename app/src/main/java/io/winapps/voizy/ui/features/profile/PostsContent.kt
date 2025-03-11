package io.winapps.voizy.ui.features.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.winapps.voizy.SessionViewModel
import io.winapps.voizy.data.model.posts.ListPost
import io.winapps.voizy.ui.theme.Ubuntu

@Composable
fun PostsContent(
) {
    val postsViewModel = hiltViewModel<PostsViewModel>()
    val sessionViewModel = hiltViewModel<SessionViewModel>()

    val posts = postsViewModel.posts
    val isLoading = postsViewModel.isLoading
    val errorMessage = postsViewModel.errorMessage

    LaunchedEffect(Unit) {
        val userId = sessionViewModel.userId ?: -1
        val apiKey = sessionViewModel.getApiKey().orEmpty()
        postsViewModel.loadPosts(
            userId = userId,
            apiKey = apiKey,
            limit = 20,
            page = 1
        )
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        when {
            isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Color(0xFFF10E91)
                )
            }
            errorMessage != null -> {
                Text(
                    text = "Error: $errorMessage",
                    modifier = Modifier.align(Alignment.Center),
                    fontFamily = Ubuntu,
                    fontWeight = FontWeight.Normal
                )
            }
            else -> {
                LazyColumn {
                    items(posts) { post ->
                        PostItem(post)
                    }
                }
            }
        }
    }
}

@Composable
fun PostItem(post: ListPost) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Color.White)
    ) {
        Text(
            text = "PostID = ${post.postID}"
        )
        Text(
            text = "UserID = ${post.userID}"
        )
        Text(
            text = "ContentText = ${post.contentText}"
        )
    }
}
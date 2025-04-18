package io.winapps.voizy.ui.home

import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import io.winapps.voizy.R
import io.winapps.voizy.SessionViewModel
import io.winapps.voizy.data.model.posts.CompletePost
import io.winapps.voizy.ui.features.profile.Comments
import io.winapps.voizy.ui.features.profile.CreatePostDialog
import io.winapps.voizy.ui.features.profile.PostItem
import io.winapps.voizy.ui.features.profile.PostsContent
import io.winapps.voizy.ui.features.profile.PostsViewModel
import io.winapps.voizy.ui.navigation.BottomNavBar
import io.winapps.voizy.ui.theme.Ubuntu

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen() {
    val sessionViewModel = hiltViewModel<SessionViewModel>()
    val userId = sessionViewModel.userId ?: -1
    val apiKey = sessionViewModel.getApiKey().orEmpty()
    val token = sessionViewModel.getToken().orEmpty()
    val configuration = LocalConfiguration.current
    val horizontalPadding = if (configuration.screenWidthDp >= 600) 200.dp else 10.dp
    val homeViewModel = hiltViewModel<HomeViewModel>()
    val postsViewModel = hiltViewModel<PostsViewModel>()
    val view = LocalView.current
    val isCreatingNewPost = postsViewModel.isCreatingNewPost
    var isViewingComments by remember { mutableStateOf(false) }
    var selectedPostID by remember { mutableLongStateOf(0) }

    val posts = homeViewModel.recommendedPosts
    val isLoading = homeViewModel.isLoading
    val errorMessage = homeViewModel.errorMessage

    SideEffect {
        val activity = view.context as Activity
        activity.window.statusBarColor = Color(0xFFF9D841).toArgb()
        WindowCompat.getInsetsController(activity.window, view).isAppearanceLightStatusBars = true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFDF4C9))
            .statusBarsPadding()
            .imePadding()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF9D841))
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.padding(4.dp)
            ) {
                Box(
                    modifier = Modifier.padding(end = 4.dp)
                ) {
                    Card(
                        elevation = CardDefaults.cardElevation(12.dp),
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .border(1.dp, Color(0xFFF10E91), RoundedCornerShape(12.dp))
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.voizy_full_square),
                            contentDescription = "Voizy full logo",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
//                                .border(1.dp, Color(0xFFF10E91), RoundedCornerShape(12.dp))
//                                .shadow(elevation = 12.dp, shape = RoundedCornerShape(12.dp))
                        )
                    }
                }

                OutlinedTextField(
                    value = homeViewModel.searchText,
                    onValueChange = {
                        homeViewModel.onSearchTextChanged(it)
                    },
                    label = { Text("Search Voizy...", fontFamily = Ubuntu, fontWeight = FontWeight.Normal) },
                    trailingIcon = {
                        IconButton(
                            onClick = {}
                        ) {
                            Icon(
                                Icons.Filled.Search,
                                contentDescription = "Search",
                                tint = Color(0xFFF10E91)
                            )
                        }
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        focusedTextColor = Color.Black,
                        focusedLabelColor = Color.DarkGray,
                        unfocusedContainerColor = Color.White,
                        unfocusedTextColor = Color.Black,
                        unfocusedLabelColor = Color.DarkGray
                    )
                )
            }
        }

        Divider(
            color = Color(0xFFF10E91),
            modifier = Modifier
                .height(1.dp)
                .fillMaxWidth()
        )

        Box(
            modifier = Modifier.weight(1f)
        ) {
            RecommendedFeed(
                posts = posts,
                load = {
                    homeViewModel.loadRecommendedPosts(
                        userId = userId,
                        apiKey = apiKey,
                        limit = 50,
                        page = 1,
                        excludeSeen = false,
                        forceRefresh = false
                    )
                },
                onSelectViewPostComments = { postID ->
                    selectedPostID = postID
                    isViewingComments = true
                },
                onCreatePost = {
                    postsViewModel.onOpenCreatePost()
                },
                isLoading = isLoading,
                errorMessage = errorMessage,
                userId = userId,
                apiKey = apiKey,
                token = token
            )
        }

        BottomNavBar()
    }

    if (isCreatingNewPost) {
        CreatePostDialog(
            onClose = { postsViewModel.onCloseCreatePost() },
            postsViewModel = postsViewModel,
            sessionViewModel = sessionViewModel
        )
    }

    if (isViewingComments) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.4f))
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {}
        ) {
            Card(
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(7.dp),
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(4.dp)
                    .systemBarsPadding()
                    .imePadding()
                    .fillMaxWidth()
                    .border(2.dp, Color(0xFFF10E91), RoundedCornerShape(12.dp)),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFDF4C9))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.CenterHorizontally)
                            .padding(8.dp)
                    ) {
                        Text(
                            text = "Comments",
                            style = androidx.compose.material3.MaterialTheme.typography.headlineSmall.copy(
                                fontFamily = Ubuntu,
                                fontWeight = FontWeight.Bold
                            ),
                            color = Color.Black,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f, fill = false)
                            .fillMaxWidth()
                    ) {
                        Comments(
                            postID = selectedPostID,
                            onClose = {
                                isViewingComments = false
                                selectedPostID = 0
                            }
                        )
                    }

                    OutlinedTextField(
                        value = postsViewModel.commentText,
                        onValueChange = { postsViewModel.onChangeCommentText(it) },
                        label = { Text("What are your thoughts?", fontFamily = Ubuntu, fontWeight = FontWeight.Normal) },
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            focusedTextColor = Color.Black,
                            focusedLabelColor = Color.DarkGray,
                            unfocusedContainerColor = Color.White,
                            unfocusedTextColor = Color.Black,
                            unfocusedLabelColor = Color.DarkGray
                        )
                    )

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(4.dp).fillMaxWidth()
                    ) {
                        TextButton(
                            onClick = {
                                isViewingComments = false
                                selectedPostID = 0
                            }
                        ) {
                            Text(
                                text = "Close",
                                style = androidx.compose.material3.MaterialTheme.typography.bodyMedium.copy(
                                    fontFamily = Ubuntu,
                                    fontWeight = FontWeight.Bold
                                ),
                                color = Color(0xFFF10E91)
                            )
                        }

                        if (postsViewModel.isPuttingNewComment) {
                            CircularProgressIndicator(
                                color = Color(0xFFF10E91)
                            )
                        } else {
                            Button(
                                onClick = {
                                    postsViewModel.putPostComment(
                                        userId = sessionViewModel.userId ?: 0,
                                        apiKey = sessionViewModel.getApiKey() ?: "",
                                        token = sessionViewModel.getToken() ?: "",
                                        postId = selectedPostID,
                                    )
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF10E91))
                            ) {
                                Text(
                                    text = "Comment",
                                    style = androidx.compose.material3.MaterialTheme.typography.bodyMedium.copy(
                                        fontFamily = Ubuntu,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = Color(0xFFFFD5ED)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RecommendedFeed(
    posts: List<CompletePost>,
    load: () -> Unit,
    onSelectViewPostComments: (Long) -> Unit,
    onCreatePost: () -> Unit,
    isLoading: Boolean,
    errorMessage: String?,
    userId: Long,
    apiKey: String,
    token: String
) {
    val postsViewModel = hiltViewModel<PostsViewModel>()

    LaunchedEffect(Unit) {
        load()
    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Button(
            onClick = { onCreatePost() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 8.dp, vertical = 2.dp
                ),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF10E91))
        ) {
            Text(
                text = "Create a new post",
                fontFamily = Ubuntu,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFD5ED)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Send,
                contentDescription = "Create post button",
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
                            PostItem(
                                post = post,
                                onReaction = { reactionType ->
                                    postsViewModel.putPostReaction(
                                        postId = post.post.postID,
                                        userId = userId,
                                        apiKey = apiKey,
                                        token = token,
                                        reactionType = reactionType,
                                    )
                                },
                                onViewComments = {
                                    onSelectViewPostComments(post.post.postID)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
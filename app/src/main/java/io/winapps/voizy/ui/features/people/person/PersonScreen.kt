package io.winapps.voizy.ui.features.people.person

import android.app.Activity
import android.os.Build
import android.view.LayoutInflater
import androidx.annotation.OptIn
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Comment
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.PersonAddDisabled
import androidx.compose.material.icons.filled.PersonOff
import androidx.compose.material.icons.filled.PersonRemove
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.DefaultTimeBar
import androidx.media3.ui.PlayerView
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import io.winapps.voizy.R
import io.winapps.voizy.SessionViewModel
import io.winapps.voizy.ui.features.profile.AboutContent
import io.winapps.voizy.ui.features.profile.Comments
import io.winapps.voizy.ui.features.profile.CreatePostDialog
import io.winapps.voizy.ui.features.profile.FriendsContent
import io.winapps.voizy.ui.features.profile.FullScreenImageViewer
import io.winapps.voizy.ui.features.profile.PhotosContent
import io.winapps.voizy.ui.features.profile.PostsContent
import io.winapps.voizy.ui.features.profile.PostsViewModel
import io.winapps.voizy.ui.features.profile.ProfileTab
import io.winapps.voizy.ui.features.profile.ProfileTabsRow
import io.winapps.voizy.ui.navigation.BottomNavBar
import io.winapps.voizy.ui.theme.Ubuntu
import io.winapps.voizy.util.GetDisplayName

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(UnstableApi::class)
@Composable
fun PersonScreen() {
    val context = LocalContext.current
    val sessionViewModel = hiltViewModel<SessionViewModel>()
    val userId = sessionViewModel.userId ?: 0
    val apiKey = sessionViewModel.getApiKey() ?: ""
    val token = sessionViewModel.getToken() ?: ""
    val personViewModel = hiltViewModel<PersonViewModel>()
    val personId = personViewModel.selectedPersonId
    val personPostsViewModel = hiltViewModel<PersonPostsViewModel>()
    val personFriendsViewModel = hiltViewModel<PersonFriendsViewModel>()
    val personPhotosViewModel = hiltViewModel<PersonPhotosViewModel>()
    val personPlayerViewModel = hiltViewModel<PersonPlayerViewModel>()
    val postsViewModel = hiltViewModel<PostsViewModel>()
    var selectedTab by remember { mutableStateOf(ProfileTab.POSTS) }
    val (isFullScreenImageOpen, setFullScreenImageOpen) = rememberSaveable { mutableStateOf(false) }
    val (currentImageIndex, setCurrentImageIndex) = rememberSaveable { mutableIntStateOf(0) }
    val isCreatingNewPost = personPostsViewModel.isCreatingNewPost
    val images = personPhotosViewModel.userImages
    var isViewingComments by remember { mutableStateOf(false) }
    var selectedPostIDForComments by remember { mutableLongStateOf(0) }
    val exoPlayer = remember { personPlayerViewModel.getExoPlayer(context) }
    val isPlaying = personPlayerViewModel.isPlaying
    val currentPosition = personPlayerViewModel.currentPosition
    val duration = personPlayerViewModel.duration
    val view = LocalView.current
    val friendStatus = personFriendsViewModel.friendStatus

    SideEffect {
        val activity = view.context as Activity
        activity.window.statusBarColor = Color.Transparent.toArgb()
        WindowCompat.getInsetsController(activity.window, view).isAppearanceLightStatusBars = false
    }

    LaunchedEffect(Unit) {
        personViewModel.loadCoverPic(personId = personId, userId = userId, apiKey = apiKey)
        personViewModel.loadProfilePic(personId = personId, userId = userId, apiKey = apiKey)
        personViewModel.loadProfileInfo(personId = personId, userId = userId, apiKey = apiKey)
        personFriendsViewModel.loadFriendStatus(personId = personId, userId = userId, apiKey = apiKey)
        personFriendsViewModel.loadTotalFriends(personId = personId, userId = userId, apiKey = apiKey)
        personPostsViewModel.loadTotalPosts(personId = personId, userId = userId, apiKey = apiKey)
        personPhotosViewModel.loadTotalImages(personId = personId, userId = userId, apiKey = apiKey)
        personPhotosViewModel.loadUserImages(personId = personId, userId = userId, apiKey = apiKey, limit = 40, page = 1)
    }

    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xFFFDF4C9)),
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().height(180.dp)
        ) {
            if (!personViewModel.coverPicURL.isNullOrEmpty()) {
                val painter = rememberAsyncImagePainter(
                    model = personViewModel.coverPicURL
                )
                Image(
                    painter = painter,
                    contentDescription = "Cover photo",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                if (painter.state is AsyncImagePainter.State.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = Color(0xFFF10E91)
                    )
                }
            } else {
                Image(
                    painter = painterResource(id = R.drawable.default_cover_pic),
                    contentDescription = "Cover photo",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .offset(x = 16.dp, y = 40.dp)
            ) {
                if (!personViewModel.profilePicURL.isNullOrEmpty()) {
                    val painter = rememberAsyncImagePainter(
                        model = personViewModel.profilePicURL
                    )
                    Image(
                        painter = painter,
                        contentDescription = "Profile photo",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFFD5ED))
                            .border(2.dp, Color(0xFFF10E91), CircleShape),
                        contentScale = ContentScale.Crop
                    )

                    if (painter.state is AsyncImagePainter.State.Loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center),
                            color = Color(0xFFF10E91)
                        )
                    }
                } else {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Empty poster profile pic",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .align(Alignment.Center)
                            .background(Color(0xFFFFD5ED))
                            .border(2.dp, Color(0xFFF10E91), CircleShape),
                        tint = Color(0xFFF10E91)
                    )
                }
            }

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = (-6).dp, y = 40.dp)
                    .padding(4.dp)
            ) {
                when(friendStatus) {
                    FriendStatus.IDLE -> {
                        Button(
                            onClick = {},
                            modifier = Modifier
                                .padding(
                                    horizontal = 4.dp, vertical = 1.dp
                                ).align(Alignment.End),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF10E91))
                        ) {
                            Text(
                                text = "Request",
                                fontFamily = Ubuntu,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFFD5ED)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Filled.PersonAdd,
                                contentDescription = "Add friend",
                                tint = Color(0xFFFFD5ED)
                            )
                        }
                    }
                    FriendStatus.PENDING -> {
                        Button(
                            onClick = {},
                            modifier = Modifier
                                .padding(
                                    horizontal = 4.dp, vertical = 1.dp
                                ).align(Alignment.End),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF10E91))
                        ) {
                            Text(
                                text = "Cancel",
                                fontFamily = Ubuntu,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFFD5ED)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Filled.PersonAddDisabled,
                                contentDescription = "Cancel friend request",
                                tint = Color(0xFFFFD5ED)
                            )
                        }
                    }
                    FriendStatus.ACCEPTED -> {
                        Button(
                            onClick = {},
                            modifier = Modifier
                                .padding(
                                    horizontal = 4.dp, vertical = 1.dp
                                ).align(Alignment.End),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF10E91))
                        ) {
                            Text(
                                text = "Unfriend",
                                fontFamily = Ubuntu,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFFD5ED)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Filled.PersonRemove,
                                contentDescription = "Remove friend (unfriend)",
                                tint = Color(0xFFFFD5ED)
                            )
                        }
                    }
                    FriendStatus.BLOCKED -> {
                        Button(
                            onClick = {},
                            modifier = Modifier
                                .padding(
                                    horizontal = 4.dp, vertical = 1.dp
                                ).align(Alignment.End),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                        ) {
                            Text(
                                text = "(Disabled)",
                                fontFamily = Ubuntu,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Filled.PersonOff,
                                contentDescription = "Add friend is disabled",
                                tint = Color.White
                            )
                        }
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            if (isPlaying) {
                                exoPlayer.pause()
                            } else {
                                exoPlayer.play()
                            }
                        },
                        modifier = Modifier.size(40.dp).clip(CircleShape),
                        colors = IconButtonDefaults.iconButtonColors(containerColor = Color(0xFFF10E91))
                    ) {
                        Icon(
                            imageVector = if (isPlaying) {
                                Icons.Filled.Pause
                            } else {
                                Icons.Filled.PlayArrow
                            },
                            contentDescription = if (isPlaying) "Pause" else "Play",
                            modifier = Modifier.size(40.dp),
                            tint = Color(0xFFFFD5ED)
                        )
                    }
                    AndroidView(
                        factory = { ctx ->
                            val view = LayoutInflater.from(ctx).inflate(R.layout.custom_media3_player_view, null) as PlayerView
                            val timeBar = view.findViewById<DefaultTimeBar>(R.id.exo_progress)

                            timeBar.setPlayedColor(ContextCompat.getColor(ctx, R.color.pale_yellow))
                            timeBar.setBufferedColor(ContextCompat.getColor(ctx, R.color.pale_magenta))
                            timeBar.setUnplayedColor(ContextCompat.getColor(ctx, R.color.dark_gray))
                            timeBar.setScrubberColor(ContextCompat.getColor(ctx, R.color.vibrant_yellow))

                            view.player = exoPlayer
                            view.useController = true
                            view.controllerAutoShow = true
                            view.controllerHideOnTouch = false
                            view.controllerShowTimeoutMs = 0
                            view.showController()
                            view
                        },
                        modifier = Modifier
                            .height(40.dp)
                            .width(190.dp)
                            .clip(RoundedCornerShape(12.dp))
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val displayName = GetDisplayName(
                username = personViewModel.username,
                preferredName = personViewModel.preferredName,
                firstName = personViewModel.firstName,
                lastName = personViewModel.lastName
            )
            Text(
                text = displayName,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontFamily = Ubuntu,
                    fontWeight = FontWeight.Bold
                ),
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (personFriendsViewModel.isLoadingTotalFriends) {
                    CircularProgressIndicator(
                        color = Color(0xFFF10E91)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = "${personFriendsViewModel.totalFriends} Friends",
                        fontFamily = Ubuntu,
                        fontWeight = FontWeight.Normal
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                if (personPostsViewModel.isLoadingTotalPosts) {
                    CircularProgressIndicator(
                        color = Color(0xFFF10E91)
                    )
                } else {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Comment,
                        contentDescription = null
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = "${personPostsViewModel.totalPosts} Posts",
                        fontFamily = Ubuntu,
                        fontWeight = FontWeight.Normal
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                if (personPhotosViewModel.isLoadingTotalImages) {
                    CircularProgressIndicator(
                        color = Color(0xFFF10E91)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Filled.PhotoLibrary,
                        contentDescription = null
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = "${personPhotosViewModel.totalImages} Photos",
                        fontFamily = Ubuntu,
                        fontWeight = FontWeight.Normal
                    )
                }
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
                ProfileTab.POSTS -> PersonPostsContent(
                    onSelectViewPostComments = { postID ->
                        selectedPostIDForComments = postID
                        isViewingComments = true
                    }
                )
                ProfileTab.PHOTOS -> PersonPhotosContent(
                    images = images,
                    setFullScreenImageOpen = setFullScreenImageOpen,
                    setCurrentImageIndex = setCurrentImageIndex
                )
                ProfileTab.ABOUT -> PersonAboutContent()
                ProfileTab.FRIENDS -> PersonFriendsContent()
            }
        }

        BottomNavBar()
    }

    if (isFullScreenImageOpen) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .zIndex(9999f)
        ) {
            FullScreenPersonImageViewer(
                images = images,
                startIndex = currentImageIndex,
                onClose = {
                    setFullScreenImageOpen(false)
                }
            )
        }
    }

    if (isCreatingNewPost) {
        PostToPersonDialog(
            onClose = { personPostsViewModel.onCloseCreatePost() },
            postsViewModel = postsViewModel,
            personPostsViewModel = personPostsViewModel,
            personViewModel = personViewModel,
            sessionViewModel = sessionViewModel,
            personId = personId
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
                            style = MaterialTheme.typography.headlineSmall.copy(
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
                            postID = selectedPostIDForComments,
                            onClose = {
                                isViewingComments = false
                                selectedPostIDForComments = 0
                            }
                        )
                    }

                    OutlinedTextField(
                        value = personPostsViewModel.commentText,
                        onValueChange = { personPostsViewModel.onChangeCommentText(it) },
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
                                selectedPostIDForComments = 0
                            }
                        ) {
                            Text(
                                text = "Close",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontFamily = Ubuntu,
                                    fontWeight = FontWeight.Bold
                                ),
                                color = Color(0xFFF10E91)
                            )
                        }

                        if (personPostsViewModel.isPuttingNewComment) {
                            CircularProgressIndicator(
                                color = Color(0xFFF10E91)
                            )
                        } else {
                            Button(
                                onClick = {
                                    personPostsViewModel.putPostComment(
                                        userId = sessionViewModel.userId ?: 0,
                                        apiKey = sessionViewModel.getApiKey() ?: "",
                                        token = sessionViewModel.getToken() ?: "",
                                        postId = selectedPostIDForComments,
                                    )
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF10E91))
                            ) {
                                Text(
                                    text = "Comment",
                                    style = MaterialTheme.typography.bodyMedium.copy(
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
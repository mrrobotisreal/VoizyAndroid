package io.winapps.voizy.ui.features.profile

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.Toast
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
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.ModeEdit
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
import io.winapps.voizy.ui.navigation.BottomNavBar
import io.winapps.voizy.ui.theme.Ubuntu
import io.winapps.voizy.ui.theme.getColorResource
import io.winapps.voizy.util.GetDisplayName

@SuppressLint("ResourceType")
@OptIn(UnstableApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProfileScreen() {
    val context = LocalContext.current
    val profileViewModel = hiltViewModel<ProfileViewModel>()
    val postsViewModel = hiltViewModel<PostsViewModel>()
    val friendsViewModel = hiltViewModel<FriendsViewModel>()
    val photosViewModel = hiltViewModel<PhotosViewModel>()
    val sessionViewModel = hiltViewModel<SessionViewModel>()
    val primaryAccent = sessionViewModel.profileColors.primaryAccent
    val secondaryColor = sessionViewModel.profileColors.secondaryColor
    val secondaryAccent = sessionViewModel.profileColors.secondaryAccent
    val playerPrimaryColor = sessionViewModel.profilePlayerColors.primaryColor
    val playerPrimaryAccent = sessionViewModel.profilePlayerColors.primaryAccent
    val playerSecondaryAccent = sessionViewModel.profilePlayerColors.secondaryAccent
    val playerViewModel = hiltViewModel<PlayerViewModel>()
    val userId = sessionViewModel.userId ?: 0
    val apiKey = sessionViewModel.getApiKey() ?: ""
    val token = sessionViewModel.getToken() ?: ""
    var selectedTab by remember { mutableStateOf(ProfileTab.POSTS) }
    val (isFullScreenImageOpen, setFullScreenImageOpen) = rememberSaveable { mutableStateOf(false) }
    val (currentImageIndex, setCurrentImageIndex) = rememberSaveable { mutableIntStateOf(0) }
    val isCreatingNewPost = postsViewModel.isCreatingNewPost
    val images = photosViewModel.userImages
    var isViewingComments by remember { mutableStateOf(false) }
    var selectedPostIDForComments by remember { mutableLongStateOf(0) }
    val exoPlayer = remember { playerViewModel.getExoPlayer(context) }
    val isPlaying = playerViewModel.isPlaying
    val currentPosition = playerViewModel.currentPosition
    val duration = playerViewModel.duration
    val view = LocalView.current

    SideEffect {
        val activity = view.context as Activity
        activity.window.statusBarColor = Color.Transparent.toArgb()
        WindowCompat.getInsetsController(activity.window, view).isAppearanceLightStatusBars = false
    }

    LaunchedEffect(Unit) {
        val userId = sessionViewModel.userId ?: -1
        val apiKey = sessionViewModel.getApiKey().orEmpty()
        profileViewModel.loadCoverPic(
            userId = userId,
            apiKey = apiKey
        )
        profileViewModel.loadProfilePic(
            userId = userId,
            apiKey = apiKey
        )
        profileViewModel.loadProfileInfo(
            userId = userId,
            apiKey = apiKey
        )
        friendsViewModel.loadTotalFriends(
            userId = userId,
            apiKey = apiKey
        )
        postsViewModel.loadTotalPosts(
            userId = userId,
            apiKey = apiKey
        )
        photosViewModel.loadTotalImages(
            userId = userId,
            apiKey = apiKey
        )
        photosViewModel.loadUserImages(
            userId = userId,
            apiKey = apiKey,
            limit = 40,
            page = 1,
        )
    }

    Column(
        modifier = Modifier.fillMaxSize().background(primaryAccent),
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().height(180.dp),
        ) {
            if (!profileViewModel.coverPicURL.isNullOrEmpty()) {
                val painter = rememberAsyncImagePainter(
                    model = profileViewModel.coverPicURL
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
                        color = secondaryColor
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
                if (!profileViewModel.profilePicURL.isNullOrEmpty()) {
                    val painter = rememberAsyncImagePainter(
                        model = profileViewModel.profilePicURL
                    )
                    Image(
                        painter = painter,
                        contentDescription = "Profile photo",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(secondaryAccent)
                            .border(2.dp, secondaryColor, CircleShape),
                        contentScale = ContentScale.Crop
                    )

                    if (painter.state is AsyncImagePainter.State.Loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center),
                            color = secondaryColor
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
                            .background(secondaryAccent)
                            .border(2.dp, secondaryColor, CircleShape),
                        tint = secondaryColor
                    )
                }
            }

            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.align(Alignment.BottomEnd).offset(x = (-6).dp, y = 40.dp)
                    .padding(4.dp)
            ) {
                Button(
                    onClick = {},
                    modifier = Modifier
                        .padding(
                            horizontal = 4.dp, vertical = 1.dp
                        ).align(Alignment.End),
                    colors = ButtonDefaults.buttonColors(containerColor = secondaryColor)
                ) {
                    Text(
                        text = "Edit",
                        fontFamily = Ubuntu,
                        fontWeight = FontWeight.Bold,
                        color = secondaryAccent
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Filled.ModeEdit,
                        contentDescription = "Edit Profile",
                        tint = secondaryAccent
                    )
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
                        colors = IconButtonDefaults.iconButtonColors(containerColor = secondaryColor)
                    ) {
                        Icon(
                            imageVector = if (isPlaying) {
                                Icons.Filled.Pause
                            } else {
                                Icons.Filled.PlayArrow
                            },
                            contentDescription = if (isPlaying) "Pause" else "Play",
                            modifier = Modifier.size(40.dp),
                            tint = secondaryAccent
                        )
                    }
                    AndroidView(
                        factory = { ctx ->
                            val view = LayoutInflater.from(ctx).inflate(R.layout.custom_media3_player_view, null) as PlayerView
                            val timeBar = view.findViewById<DefaultTimeBar>(R.id.exo_progress)

                            timeBar.setPlayedColor(ContextCompat.getColor(ctx, playerPrimaryAccent))
                            timeBar.setBufferedColor(ContextCompat.getColor(ctx, playerSecondaryAccent))
                            timeBar.setUnplayedColor(ContextCompat.getColor(ctx, R.color.dark_gray))
                            timeBar.setScrubberColor(ContextCompat.getColor(ctx, playerPrimaryColor))

                            view.player = exoPlayer
                            view.useController = true
                            view.controllerAutoShow = true
                            view.controllerHideOnTouch = false
                            view.controllerShowTimeoutMs = 0
                            view.showController()
                            view
                        },
                        update = { view ->
                            val bg = secondaryColor.toArgb()
                            val controllerFrame =
                                view.findViewById<FrameLayout>(R.id.media3_controller)
                            controllerFrame.setBackgroundColor(bg)
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
                username = sessionViewModel.username ?: "",
                preferredName = profileViewModel.preferredName,
                firstName = profileViewModel.firstName,
                lastName = profileViewModel.lastName
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
                if (friendsViewModel.isLoadingTotalFriends) {
                    CircularProgressIndicator(
                        color = secondaryColor
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null
                    )
                    Spacer(Modifier.width(4.dp))
                    val totalFriendsText = if (friendsViewModel.totalFriends.toInt() == 0) {
                        "No friends yet"
                    } else if (friendsViewModel.totalFriends.toInt() == 1) {
                        "1 Friend"
                    } else {
                        "${friendsViewModel.totalFriends} Friends"
                    }
                    Text(
                        text = totalFriendsText,
                        fontFamily = Ubuntu,
                        fontWeight = FontWeight.Normal
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                if (postsViewModel.isLoadingTotalPosts) {
                    CircularProgressIndicator(
                        color = secondaryColor
                    )
                } else {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Comment,
                        contentDescription = null
                    )
                    Spacer(Modifier.width(4.dp))
                    val totalPostsText = if (postsViewModel.totalPosts.toInt() == 0) {
                        "No posts"
                    } else if (postsViewModel.totalPosts.toInt() == 1) {
                        "1 Post"
                    } else {
                        "${postsViewModel.totalPosts} Posts"
                    }
                    Text(
                        text = totalPostsText,
                        fontFamily = Ubuntu,
                        fontWeight = FontWeight.Normal
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                if (photosViewModel.isLoadingTotalImages) {
                    CircularProgressIndicator(
                        color = secondaryColor
                    )
                } else {
                    Icon(
                        imageVector = Icons.Filled.PhotoLibrary,
                        contentDescription = null
                    )
                    Spacer(Modifier.width(4.dp))
                    val totalPhotosText = if (photosViewModel.totalImages.toInt() == 0) {
                        "No photos"
                    } else if (photosViewModel.totalImages.toInt() == 1) {
                        "1 Photo"
                    } else {
                        "${photosViewModel.totalImages} Photos"
                    }
                    Text(
                        text = totalPhotosText,
                        fontFamily = Ubuntu,
                        fontWeight = FontWeight.Normal
                    )
                }
            }
        }

        ProfileTabsRow(
            selectedTab = selectedTab,
            onTabSelected = { selectedTab = it },
            secondaryColor = secondaryColor,
            secondaryAccent = secondaryAccent
        )

        Box(
            modifier = Modifier.weight(1f)
        ) {
            when (selectedTab) {
                ProfileTab.POSTS -> PostsContent(
                    onSelectViewPostComments = { postID ->
                        selectedPostIDForComments = postID
                        isViewingComments = true
                    },
                    secondaryColor = secondaryColor,
                    secondaryAccent = secondaryAccent
                )
                ProfileTab.PHOTOS -> PhotosContent(
                    images = images,
                    setFullScreenImageOpen = setFullScreenImageOpen,
                    setCurrentImageIndex = setCurrentImageIndex,
                    secondaryColor = secondaryColor,
                    secondaryAccent = secondaryAccent
                )
                ProfileTab.ABOUT -> AboutContent(
                    secondaryColor = secondaryColor
                )
                ProfileTab.FRIENDS -> FriendsContent(
                    secondaryColor = secondaryColor,
                    secondaryAccent = secondaryAccent
                )
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
            FullScreenImageViewer(
                images = images,
                startIndex = currentImageIndex,
                onClose = {
                    setFullScreenImageOpen(false)
                },
                secondaryColor = secondaryColor,
                secondaryAccent = secondaryAccent
            )
        }
    }

    if (isCreatingNewPost) {
        CreatePostDialog(
            onClose = { postsViewModel.onCloseCreatePost() },
            postsViewModel = postsViewModel,
            sessionViewModel = sessionViewModel,
            primaryAccent = primaryAccent,
            secondaryColor = secondaryColor,
            secondaryAccent = secondaryAccent
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
//                    .systemBarsPadding()
//                    .imePadding()
                    .fillMaxWidth()
                    .border(2.dp, secondaryColor, RoundedCornerShape(12.dp)),
                colors = CardDefaults.cardColors(containerColor = primaryAccent)
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
                            },
                            secondaryColor = secondaryColor,
                            secondaryAccent = secondaryAccent
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
                                selectedPostIDForComments = 0
                            }
                        ) {
                            Text(
                                text = "Close",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontFamily = Ubuntu,
                                    fontWeight = FontWeight.Bold
                                ),
                                color = secondaryColor
                            )
                        }

                        if (postsViewModel.isPuttingNewComment) {
                            CircularProgressIndicator(
                                color = secondaryColor
                            )
                        } else {
                            Button(
                                onClick = {
                                    postsViewModel.putPostComment(
                                        userId = sessionViewModel.userId ?: 0,
                                        apiKey = sessionViewModel.getApiKey() ?: "",
                                        token = sessionViewModel.getToken() ?: "",
                                        postId = selectedPostIDForComments,
                                    )
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = secondaryColor)
                            ) {
                                Text(
                                    text = "Comment",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontFamily = Ubuntu,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = secondaryAccent
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (photosViewModel.isAddingImages) {
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
                    .border(2.dp, secondaryColor, RoundedCornerShape(12.dp)),
                colors = CardDefaults.cardColors(containerColor = primaryAccent)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally)
                        .padding(8.dp)
                ) {
                    Text(
                        text = "Add Profile Images",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontFamily = Ubuntu,
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color.Black,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                Card(
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(1.dp),
                    modifier = Modifier
                        .padding(8.dp)
                        .weight(1F)
                        .border(2.dp, secondaryColor, RoundedCornerShape(12.dp)),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column {
                        if (photosViewModel.selectedImages.isNotEmpty()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                                    .align(Alignment.CenterHorizontally)
                                    .padding(8.dp)
                            ) {
                                AddImagesCarousel(
                                    imageUrls = photosViewModel.selectedImages,
                                    onRemove = { uri ->
                                        photosViewModel.removeImage(uri)
                                    },
                                    heightDp = 400.dp
                                )
                            }
                        } else {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                                    .align(Alignment.CenterHorizontally)
                                    .padding(8.dp)
                            ) {
                                Text(
                                    text = "No images added yet",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontFamily = Ubuntu,
                                        fontWeight = FontWeight.Normal
                                    ),
                                    color = Color.Black,
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }
                        }

                        CameraButtonWithPermission(
                            onPhotoCaptured = { uri ->
                                photosViewModel.addImage(uri)
                            },
                            secondaryColor = secondaryColor,
                            secondaryAccent = secondaryAccent
                        )
                        PhotosButtonWithPermission(
                            onPhotosSelected = { uris ->
                                photosViewModel.addImages(uris)
                            },
                            secondaryColor = secondaryColor,
                            secondaryAccent = secondaryAccent
                        )
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(4.dp).fillMaxWidth()
                ) {
                    TextButton(
                        onClick = {
                            photosViewModel.onCloseAddImages()
                        }
                    ) {
                        Text(
                            text = "Close",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontFamily = Ubuntu,
                                fontWeight = FontWeight.Bold
                            ),
                            color = secondaryColor
                        )
                    }

                    if (photosViewModel.isPuttingNewImages) {
                        CircularProgressIndicator(
                            color = secondaryColor
                        )
                    } else {
                        Button(
                            onClick = {
                                if (photosViewModel.selectedImages.isEmpty()) {
                                    return@Button
                                }
                                photosViewModel.putUserImages(
                                    context = context,
                                    userId = sessionViewModel.userId ?: 0,
                                    apiKey = sessionViewModel.getApiKey() ?: "",
                                    token = sessionViewModel.getToken() ?: ""
                                )
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = secondaryColor)
                        ) {
                            Text(
                                text = "Add Images",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontFamily = Ubuntu,
                                    fontWeight = FontWeight.Bold
                                ),
                                color = secondaryAccent
                            )
                        }
                    }
                }
            }
        }
    }

    if (photosViewModel.showPutUserImagesSuccessToast) {
        Toast.makeText(context, "Successfully uploaded images!", Toast.LENGTH_SHORT).show()
        photosViewModel.loadTotalImages(userId = userId, apiKey = apiKey)
        photosViewModel.loadUserImages(
            userId = userId,
            apiKey = apiKey,
            limit = 50,
            page = 1,
        )
        photosViewModel.onCloseAddImages()
        photosViewModel.endShowPutUserImagesSuccessToast()
    }
}

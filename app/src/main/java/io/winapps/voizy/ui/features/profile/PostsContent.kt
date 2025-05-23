package io.winapps.voizy.ui.features.profile

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Comment
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import io.winapps.voizy.SessionViewModel
import io.winapps.voizy.data.model.posts.CompletePost
import io.winapps.voizy.ui.theme.Ubuntu
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.pager.HorizontalPagerIndicator
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Scale
import coil.size.Size
import io.winapps.voizy.data.model.posts.Comment
import io.winapps.voizy.data.model.posts.ReactionType
import io.winapps.voizy.util.getTimeAgo

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PostsContent(
    onSelectViewPostComments: (Long) -> Unit,
    secondaryColor: Color,
    secondaryAccent: Color
) {
    val postsViewModel = hiltViewModel<PostsViewModel>()
    val sessionViewModel = hiltViewModel<SessionViewModel>()
    val userId = sessionViewModel.userId ?: -1
    val apiKey = sessionViewModel.getApiKey().orEmpty()
    val token = sessionViewModel.getToken().orEmpty()

    val posts = postsViewModel.completePosts
    val isLoading = postsViewModel.isLoading
    val errorMessage = postsViewModel.errorMessage

    LaunchedEffect(Unit) {
        postsViewModel.loadCompletePosts(
            userId = userId,
            apiKey = apiKey,
            limit = 20,
            page = 1
        )
    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Button(
            onClick = { postsViewModel.onOpenCreatePost() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 8.dp, vertical = 2.dp
                ),
            colors = ButtonDefaults.buttonColors(containerColor = secondaryColor)
        ) {
            Text(
                text = "Create a new post",
                fontFamily = Ubuntu,
                fontWeight = FontWeight.Bold,
                color = secondaryAccent
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
                        color = secondaryColor
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
                    if (posts.isNullOrEmpty()) {
                        Text(
                            text = "You haven't posted anything yet.",
                            textAlign = TextAlign.Center,
                            fontFamily = Ubuntu,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    } else {
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
                                    },
                                    secondaryColor = secondaryColor,
                                    secondaryAccent = secondaryAccent
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
fun PostItem(
    post: CompletePost,
    onProfileClick: () -> Unit = {},
    onReaction: (ReactionType) -> Unit,
    onViewComments: () -> Unit,
    secondaryColor: Color,
    secondaryAccent: Color
) {
    val displayName = if (!post.post.firstName.isNullOrBlank() && !post.post.lastName.isNullOrBlank()) {
        "${post.post.firstName} ${post.post.lastName} (${post.post.username})"
    } else {
        "${post.post.preferredName} (${post.post.username})"
    }
    val timeAgo = if (post.post.createdAt != null) getTimeAgo(post.post.createdAt) else "Unknown time"
    val locationName = post.post.locationName
    val contentText = post.post.contentText
    val userReaction = post.post.userReaction
    val reactionCount = post.post.totalReactions
    val commentCount = post.post.totalComments
    val postSharesCount = post.post.totalPostShares

    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(secondaryAccent)
                        .border(2.dp, secondaryColor, CircleShape)
                        .clickable { onProfileClick() }
                ) {
                    if (!post.profilePicURL.isNullOrEmpty()) {
                        val painter = rememberAsyncImagePainter(
                            model = post.profilePicURL
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
                    } else {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Empty poster profile pic",
                            modifier = Modifier
                                .clip(CircleShape)
                                .align(Alignment.Center),
                            tint = secondaryColor
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = displayName,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontFamily = Ubuntu,
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color.Black
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = timeAgo,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontFamily = Ubuntu,
                                fontWeight = FontWeight.Normal
                            ),
                            color = Color.Gray
                        )

                        if (!locationName.isNullOrBlank()) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = secondaryColor,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = locationName,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontFamily = Ubuntu,
                                    fontWeight = FontWeight.Normal
                                ),
                                color = secondaryColor
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (contentText.isNotEmpty()) {
                Text(
                    text = contentText,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontFamily = Ubuntu,
                        fontWeight = FontWeight.Normal
                    ),
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            val images = post.images ?: emptyList()
            if (images.isNotEmpty()) {
                PostImagesCarousel(
                    imageUrls = images,
                    heightDp = 250.dp
                )
            }

            val hashtags = post.hashtags ?: emptyList()
            if (hashtags.isNotEmpty()) {
                PostItemWithHashtags(
                    hashtags = hashtags,
                    secondaryColor = secondaryColor,
                    secondaryAccent = secondaryAccent
                )
            }

            PostItemWithReactions(
                userReaction = userReaction,
                reactionCount = reactionCount,
                commentCount = commentCount,
                postSharesCount = postSharesCount,
                viewsCount = post.post.views,
                onReaction = onReaction,
                onViewComments = onViewComments,
                secondaryColor = secondaryColor,
                 secondaryAccent = secondaryAccent
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PostItemWithHashtags(
    hashtags: List<String>,
    secondaryColor: Color,
    secondaryAccent: Color
) {
    LazyRow(
        modifier = Modifier.padding(2.dp),
        contentPadding = PaddingValues(2.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(hashtags) { hashtag ->
            Box(
                modifier = Modifier.padding(horizontal = 2.dp)
            ) {
                HashtagButton(
                    hashtag = hashtag,
                    secondaryColor = secondaryColor,
                    secondaryAccent = secondaryAccent
                )
            }
        }
    }
}

@Composable
fun HashtagButton(
    hashtag: String,
    secondaryColor: Color,
    secondaryAccent: Color
) {
    TextButton(
        onClick = {  },
        colors = ButtonDefaults.buttonColors(containerColor = secondaryAccent),
        border = BorderStroke(1.dp, secondaryColor),
    ) {
        Text(
            text = "#$hashtag",
            color = secondaryColor
        )
    }
}

@Composable
fun HashtagButtonRemoveable(
    hashtag: String,
    onRemove: (String) -> Unit,
    secondaryColor: Color,
    secondaryAccent: Color
) {
    TextButton(
        onClick = {
            onRemove(hashtag)
        },
        colors = ButtonDefaults.buttonColors(containerColor = secondaryAccent),
        border = BorderStroke(1.dp, secondaryColor),
    ) {
        Text(
            text = "#$hashtag",
            color = secondaryColor
        )

        Icon(
            imageVector = Icons.Filled.Close,
            contentDescription = null,
            tint = Color.Red
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PostImagesCarousel(
    imageUrls: List<String>,
    modifier: Modifier = Modifier,
    heightDp: Dp = 250.dp
) {
    if (imageUrls.isEmpty()) return

    val pagerState = rememberPagerState()

    Column(modifier = modifier) {
        HorizontalPager(
            count = imageUrls.size,
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(heightDp)
        ) { page ->
            val url = imageUrls[page]
            Box(modifier = Modifier.fillMaxSize()) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(url)
                        .crossfade(true)
                        .scale(Scale.FILL)
                        .size(Size.ORIGINAL)
                        .build(),
                    contentDescription = "Image",
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.LightGray),
                    contentScale = ContentScale.Crop
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        HorizontalPagerIndicator(
            pagerState = pagerState,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(8.dp))
    }
}

fun ReactionType.emoji(): String = when(this) {
    ReactionType.LIKE -> "❤️"
    ReactionType.LOVE -> "😍"
    ReactionType.LAUGH -> "😂"
    ReactionType.CONGRATULATE -> "👏"
    ReactionType.SHOCKED -> "😮"
    ReactionType.SAD -> "😢"
    ReactionType.ANGRY -> "😡"
}

@Composable
fun PostItemWithReactions(
    userReaction: String?,
    reactionCount: Long,
    commentCount: Long,
    postSharesCount: Long,
    viewsCount: Long,
    onReaction: (ReactionType) -> Unit,
    onViewComments: () -> Unit,
    secondaryColor: Color,
    secondaryAccent: Color
) {
    var currentReaction by remember { mutableStateOf<ReactionType?>(null) }
    currentReaction = when(userReaction) {
        "like" -> ReactionType.LIKE
        "love" -> ReactionType.LOVE
        "laugh" -> ReactionType.LAUGH
        "congratulate" -> ReactionType.CONGRATULATE
        "shocked" -> ReactionType.SHOCKED
        "sad" -> ReactionType.SAD
        "angry" -> ReactionType.ANGRY
        else -> null
    }

    var isReactionMenuVisible by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box {
                ReactionIconButton(
                    currentReaction = currentReaction,
                    onShortPress = {
                        currentReaction = ReactionType.LIKE
                        onReaction(ReactionType.LIKE)
                    },
                    onLongPress = {
                        isReactionMenuVisible = true
                    },
                    secondaryColor = secondaryColor,
                    secondaryAccent = secondaryAccent
                )

                if (isReactionMenuVisible) {
                    ReactionPopUpRow(
                        onSelectReaction = { selected ->
                            currentReaction = selected
                            onReaction(selected)
                            isReactionMenuVisible = false
                        },
                        onOutsideClick = {
                            isReactionMenuVisible = false
                        },
                        secondaryColor = secondaryColor
                    )
                }
            }
            if (reactionCount > 0) {
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = reactionCount.toString(),
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontFamily = Ubuntu,
                        fontWeight = FontWeight.Normal
                    ),
                    color = Color.DarkGray
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { onViewComments() },
                colors = IconButtonDefaults.iconButtonColors(containerColor = secondaryColor),
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Comment,
                    contentDescription = null,
                    tint = secondaryAccent
                )
            }
            if (commentCount > 0) {
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = commentCount.toString(),
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontFamily = Ubuntu,
                        fontWeight = FontWeight.Normal
                    ),
                    color = Color.DarkGray
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {},
                colors = IconButtonDefaults.iconButtonColors(containerColor = secondaryColor),
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Repeat,
                    contentDescription = null,
                    tint = secondaryAccent
                )
            }
            if (postSharesCount > 0) {
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = postSharesCount.toString(),
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontFamily = Ubuntu,
                        fontWeight = FontWeight.Normal
                    ),
                    color = Color.DarkGray
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {},
                colors = IconButtonDefaults.iconButtonColors(containerColor = secondaryColor),
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.BarChart,
                    contentDescription = null,
                    tint = secondaryAccent
                )
            }
            if (viewsCount > 0) {
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = viewsCount.toString(),
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

@Composable
fun ReactionIconButton(
    currentReaction: ReactionType?,
    onShortPress: () -> Unit,
    onLongPress: () -> Unit,
    secondaryColor: Color,
    secondaryAccent: Color
) {
    val displayEmoji = currentReaction?.emoji() ?: "❤️"

    if (currentReaction == null) {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(secondaryColor)
                .size(40.dp)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            onShortPress()
                        },
                        onLongPress = {
                            onLongPress()
                        }
                    )
                }
        ) {
            Icon(
                imageVector = Icons.Filled.Favorite,
                contentDescription = null,
                tint = secondaryAccent,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    } else {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(secondaryColor)
                .size(40.dp)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            onShortPress()
                        },
                        onLongPress = {
                            onLongPress()
                        }
                    )
                }
        ) {
            Text(
                text = displayEmoji,
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 24.sp,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Composable
fun ReactionPopUpRow(
    onSelectReaction: (ReactionType) -> Unit,
    onOutsideClick: () -> Unit,
    secondaryColor: Color
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        onOutsideClick()
                    }
                )
            }
    ) {
        Card(
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 0.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Close",
                    tint = secondaryColor,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { onOutsideClick() }
                )
                val allReactions = listOf(
                    ReactionType.LIKE,
                    ReactionType.LOVE,
                    ReactionType.LAUGH,
                    ReactionType.CONGRATULATE,
                    ReactionType.SHOCKED,
                    ReactionType.SAD,
                    ReactionType.ANGRY
                )
                allReactions.forEach { reaction ->
                    Text(
                        text = reaction.emoji(),
                        fontSize = 24.sp,
                        modifier = Modifier
                            .clickable {
                                onSelectReaction(reaction)
                            }
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Comments(
    postID: Long,
    onClose: () -> Unit,
    secondaryColor: Color,
    secondaryAccent: Color
) {
    val postsViewModel = hiltViewModel<PostsViewModel>()
    val sessionViewModel = hiltViewModel<SessionViewModel>()
    val apiKey = sessionViewModel.getApiKey() ?: ""
    val userId = sessionViewModel.userId ?: -1
    val comments = postsViewModel.comments ?: emptyList<Comment>()

    LaunchedEffect(Unit) {
        postsViewModel.loadPostComments(
            userId = userId,
            apiKey = apiKey,
            postId = postID,
            20,
            1,
        )
    }

    LazyColumn {
        items(comments) { comment ->
            CommentRow(
                comment = comment,
                secondaryColor = secondaryColor,
                secondaryAccent = secondaryAccent
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CommentRow(
    comment: Comment,
    secondaryColor: Color,
    secondaryAccent: Color
) {
    val displayName = if (!comment.firstName.isNullOrBlank() && !comment.lastName.isNullOrBlank()) {
        "${comment.firstName} ${comment.lastName} (${comment.username})"
    } else {
        "${comment.preferredName} (${comment.username})"
    }
    val timeAgo = if (comment.createdAt != null) getTimeAgo(comment.createdAt) else "Unknown time"

    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(secondaryAccent)
                        .border(2.dp, secondaryColor, CircleShape)
                        .clickable {  }
                ) {
                    if (!comment.profilePicURL.isNullOrEmpty()) {
                        val painter = rememberAsyncImagePainter(
                            model = comment.profilePicURL
                        )
                        Image(
                            painter = painter,
                            contentDescription = "Commenter profile pic",
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
                    } else {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Empty commenter profile pic",
                            modifier = Modifier
                                .clip(CircleShape)
                                .align(Alignment.Center),
                            tint = secondaryColor
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = displayName,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontFamily = Ubuntu,
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color.Black
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = timeAgo,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontFamily = Ubuntu,
                                fontWeight = FontWeight.Normal
                            ),
                            color = Color.Gray
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (comment.contentText.isNotEmpty()) {
                Text(
                    text = comment.contentText,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontFamily = Ubuntu,
                        fontWeight = FontWeight.Normal
                    ),
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}
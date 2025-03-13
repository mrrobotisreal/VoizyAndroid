package io.winapps.voizy.ui.features.profile

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.winapps.voizy.R
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
import io.winapps.voizy.util.getTimeAgo

@Composable
fun PostsContent(
) {
    val postsViewModel = hiltViewModel<PostsViewModel>()
    val sessionViewModel = hiltViewModel<SessionViewModel>()

    val posts = postsViewModel.completePosts
    val isLoading = postsViewModel.isLoading
    val errorMessage = postsViewModel.errorMessage

    LaunchedEffect(Unit) {
        val userId = sessionViewModel.userId ?: -1
        val apiKey = sessionViewModel.getApiKey().orEmpty()
        postsViewModel.loadCompletePosts(
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PostItem(
    post: CompletePost,
    onProfileClick: () -> Unit = {}
) {
    val displayName = if (!post.post.firstName.isNullOrBlank() && !post.post.lastName.isNullOrBlank()) {
        "${post.post.firstName} ${post.post.lastName} (${post.post.username})"
    } else {
        "${post.post.preferredName} (${post.post.username})"
    }
    val timeAgo = if (post.post.createdAt != null) getTimeAgo(post.post.createdAt) else "Unknown time"
    val locationName = post.post.locationName
    val contentText = post.post.contentText
    val reactionCount = if (post.reactions != null) post.reactions.size else 0
    val commentCount = 0

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
                        .background(Color.Gray)
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
                                .border(2.dp, Color(0xFFF10E91), CircleShape),
                            contentScale = ContentScale.Crop
                        )

                        if (painter.state is AsyncImagePainter.State.Loading) {
                            CircularProgressIndicator(
                                modifier = Modifier.align(Alignment.Center),
                                color = Color(0xFFF10E91)
                            )
                        }
//                        AsyncImage(
//                            model = post.profilePicURL,
//                            contentDescription = "Poster profile pic",
//                            modifier = Modifier.clip(CircleShape),
//                            contentScale = ContentScale.Crop
//                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Empty poster profile pic",
                            modifier = Modifier.clip(CircleShape).align(Alignment.Center)
                        )
                    }
//                    Image(
//                        painter = painterResource(id = R.drawable.test_profile_photo),
//                        contentDescription = null,
//                        modifier = Modifier
//                            .size(40.dp)
//                            .clip(CircleShape)
//                            .border(2.dp, Color(0xFFF10E91), CircleShape),
//                        contentScale = ContentScale.Crop
//                    )
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
                                tint = Color(0xFFF10E91),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = locationName,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontFamily = Ubuntu,
                                    fontWeight = FontWeight.Normal
                                ),
                                color = Color(0xFFF10E91)
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
//                Box(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .background(Color.LightGray)
//                ) {
//                    // AsyncImage
//                    Image(
//                        painter = painterResource(id = R.drawable.default_cover2),
//                        contentDescription = "Cover photo",
//                        modifier = Modifier.fillMaxSize(),
//                        contentScale = ContentScale.Crop
//                    )
//                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {},
                        colors = IconButtonDefaults.iconButtonColors(containerColor = Color(0xFFF10E91)),
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = null,
                            tint = Color(0xFFFFD5ED)
                        )
                    }
                    if (reactionCount > 0) {
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
                        onClick = {},
                        colors = IconButtonDefaults.iconButtonColors(containerColor = Color(0xFFF10E91)),
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.List,
                            contentDescription = null,
                            tint = Color(0xFFFFD5ED)
                        )
                    }
                    if (commentCount > 0) {
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
                        colors = IconButtonDefaults.iconButtonColors(containerColor = Color(0xFFF10E91)),
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = null,
                            tint = Color(0xFFFFD5ED)
                        )
                    }
                }
            }
        }
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
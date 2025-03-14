package io.winapps.voizy.ui.features.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddLocation
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhotoAlbum
import androidx.compose.material.icons.filled.Poll
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Scale
import coil.size.Size
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import io.winapps.voizy.SessionViewModel
import io.winapps.voizy.ui.theme.Ubuntu

@Composable
fun CreatePostDialog(
    onClose: () -> Unit,
    postsViewModel: PostsViewModel,
    sessionViewModel: SessionViewModel
) {
    val userId = sessionViewModel.userId ?: -1
    val apiKey = sessionViewModel.getApiKey().orEmpty()
    val token = sessionViewModel.getToken().orEmpty()
    val username = sessionViewModel.username.orEmpty()
    val preferredName = sessionViewModel.preferredName.orEmpty()
    var isSelectingLocation by remember { mutableStateOf(false) }
    var locationName by remember { mutableStateOf("") }
    var locationLat by remember { mutableStateOf<Double?>(null) }
    var locationLong by remember { mutableStateOf<Double?>(null) }
    var selectedImages by remember { mutableStateOf<List<Uri>>(emptyList()) }

    val pickImagesLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents(),
        onResult = { uris ->
            val newList = if (uris.size > 10) uris.take(10) else uris
            selectedImages = newList
        }
    )


    LaunchedEffect(Unit) {
        postsViewModel.loadProfilePic(
            userId = userId,
            apiKey = apiKey
        )
        postsViewModel.loadProfileInfo(
            userId = userId,
            apiKey = apiKey,
            username = username,
            preferredName = preferredName
        )
    }

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
            if (!isSelectingLocation) {
                MainPostFormUI(
                    onClose = { onClose() },
                    postsViewModel = postsViewModel,
                    sessionViewModel = sessionViewModel,
                    locationName = locationName,
                    onAddLocationClick = {
//                        locationLat = postsViewModel.currentLat
//                        locationLong = postsViewModel.currentLong
                        isSelectingLocation = true
                    },
                    selectedImages = selectedImages,
                    onPhotosAlbumClick = {
                        pickImagesLauncher.launch("image/*")
                    }
                )
            } else {
                LocationPickerUI(
                    onBack = {
                        isSelectingLocation = false
                    },
                    onAdd = { newName, newLat, newLong ->
                        locationName = newName
                        locationLat = newLat
                        locationLong = newLong
                        isSelectingLocation = false
                    }
                )
            }
        }
    }
}

@Composable
fun MainPostFormUI(
    onClose: () -> Unit,
    postsViewModel: PostsViewModel,
    sessionViewModel: SessionViewModel,
    locationName: String,
    onAddLocationClick: () -> Unit,
    selectedImages: List<Uri>,
    onPhotosAlbumClick: () -> Unit
) {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Create Post",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.Black,
            fontFamily = Ubuntu,
            fontWeight = FontWeight.Bold
        )

        Card(
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(1.dp),
            modifier = Modifier
                .padding(8.dp)
                .weight(1F)
                .border(2.dp, Color(0xFFF10E91), RoundedCornerShape(12.dp)),
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
                            .background(Color(0xFFFFD5ED))
                            .border(2.dp, Color(0xFFF10E91), CircleShape)
                            .clickable {  }
                    ) {
                        if (!postsViewModel.profilePicURL.isNullOrEmpty()) {
                            val painter = rememberAsyncImagePainter(
                                model = postsViewModel.profilePicURL
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
                        } else {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Empty poster profile pic",
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .align(Alignment.Center),
                                tint = Color(0xFFF10E91)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = postsViewModel.displayName.orEmpty(),
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
                                text = "Just now",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontFamily = Ubuntu,
                                    fontWeight = FontWeight.Normal
                                ),
                                color = Color.Gray
                            )

                            if (locationName.isNotEmpty()) {
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
                            } else {
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(
                                    imageVector = Icons.Filled.AddLocation,
                                    contentDescription = null,
                                    tint = Color(0xFFF10E91),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Add location",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontFamily = Ubuntu,
                                        fontWeight = FontWeight.Normal
                                    ),
                                    color = Color(0xFFF10E91),
                                    modifier = Modifier
                                        .clickable { onAddLocationClick() }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                if (postsViewModel.postText.isNotEmpty()) {
                    Text(
                        text = postsViewModel.postText,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontFamily = Ubuntu,
                            fontWeight = FontWeight.Normal
                        ),
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                } else {
                    Box(
                        modifier = Modifier.weight(1F)
                    ) {
                        Text(
                            text = "What's on your mind?",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontFamily = Ubuntu,
                                fontWeight = FontWeight.Normal
                            ),
                            color = Color.Black
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                        if (selectedImages.isNotEmpty()) {
                            CreatePostImagesCarousel(
                                imageUrls = selectedImages,
                                heightDp = 250.dp
                            )
                        }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {},
                        colors = IconButtonDefaults.iconButtonColors(containerColor = Color(0xFFF10E91)),
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.CameraAlt,
                            contentDescription = null,
                            tint = Color(0xFFFFD5ED)
                        )
                    }

                    IconButton(
                        onClick = { onPhotosAlbumClick() },
                        colors = IconButtonDefaults.iconButtonColors(containerColor = Color(0xFFF10E91)),
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.PhotoAlbum,
                            contentDescription = null,
                            tint = Color(0xFFFFD5ED)
                        )
                    }

                    IconButton(
                        onClick = {},
                        colors = IconButtonDefaults.iconButtonColors(containerColor = Color(0xFFF10E91)),
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Tag,
                            contentDescription = null,
                            tint = Color(0xFFFFD5ED)
                        )
                    }

                    IconButton(
                        onClick = {},
                        colors = IconButtonDefaults.iconButtonColors(containerColor = Color(0xFFF10E91)),
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Groups,
                            contentDescription = null,
                            tint = Color(0xFFFFD5ED)
                        )
                    }

                    IconButton(
                        onClick = {},
                        colors = IconButtonDefaults.iconButtonColors(containerColor = Color(0xFFF10E91)),
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Poll,
                            contentDescription = null,
                            tint = Color(0xFFFFD5ED)
                        )
                    }
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = { onClose() }) {
                Text(
                    text = "Cancel",
                    color = Color(0xFFF10E91),
                    fontFamily = Ubuntu,
                    fontWeight = FontWeight.Bold
                )
            }

            if (postsViewModel.isSubmittingPost) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color(0xFFF10E91)
                )
            } else {
                Button(
                    onClick = { postsViewModel.submitPost() },
                    colors = buttonColors(containerColor = Color(0xFFF10E91))
                ) {
                    Text("Create", fontFamily = Ubuntu, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CreatePostImagesCarousel(
    imageUrls: List<Uri>,
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
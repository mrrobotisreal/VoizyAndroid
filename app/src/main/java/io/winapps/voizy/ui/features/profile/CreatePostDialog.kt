package io.winapps.voizy.ui.features.profile

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
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
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddLocation
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhotoLibrary
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
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
import java.io.File

fun createImageFileUri(context: Context): Uri? {
    val filename = "temp_photo_${System.currentTimeMillis()}.jpg"
    val tempFile = File(context.cacheDir, filename)
    return try {
        FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            tempFile
        )
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

@Composable
fun isKeyboardVisible(): Boolean {
    val density = LocalDensity.current
    val imeBottomPx = WindowInsets.ime.getBottom(density)
    return imeBottomPx > 0
}

@Composable
fun CreatePostDialog(
    onClose: () -> Unit,
    postsViewModel: PostsViewModel,
    sessionViewModel: SessionViewModel
) {
    val context = LocalContext.current
    val userId = sessionViewModel.userId ?: -1
    val apiKey = sessionViewModel.getApiKey().orEmpty()
    val token = sessionViewModel.getToken().orEmpty()
    val username = sessionViewModel.username.orEmpty()
    val preferredName = sessionViewModel.preferredName.orEmpty()
    val selectedImages = postsViewModel.selectedImages
    var isSelectingLocation by remember { mutableStateOf(false) }
    var isAddingHashtags by remember { mutableStateOf(false) }
    var locationName by remember { mutableStateOf("") }
    var locationLat by remember { mutableStateOf<Double?>(null) }
    var locationLong by remember { mutableStateOf<Double?>(null) }
    var hashtags by remember { mutableStateOf<List<String>>(emptyList<String>()) }
    var hashtagText by remember { mutableStateOf("") }

    val pickImagesLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents(),
        onResult = { uris ->
            val newList = if (uris.size > 10) uris.take(10) else uris
            postsViewModel.addImages(
                uris = newList
            )
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
            if (!isSelectingLocation && !isAddingHashtags) {
                MainPostFormUI(
                    onClose = { onClose() },
                    onSubmitPost = {
                        val locName = if (locationName.isBlank() || locationName.isEmpty()) null else locationName
                        postsViewModel.submitPost(
                            context = context,
                            apiKey = apiKey,
                            userId = userId,
                            token = token,
                            locationName = locName,
                            locationLat = locationLat,
                            locationLong = locationLong,
                            hashtags = hashtags
                        )
                    },
                    postsViewModel = postsViewModel,
                    sessionViewModel = sessionViewModel,
                    locationName = locationName,
                    onAddLocationClick = {
                        isAddingHashtags = false
                        isSelectingLocation = true
                    },
                    onAddHashtagsClick = {
                        isSelectingLocation = false
                        isAddingHashtags = true
                    },
                    hashtags = hashtags,
                    selectedImages = selectedImages,
                    onPhotoCaptured = { uri ->
                        postsViewModel.addImage(uri)
                    },
                    onPhotosAlbumClick = {
                        pickImagesLauncher.launch("image/*")
                    }
                )
            } else {
                if (isSelectingLocation) {
                    LocationPickerUI(
                        onBack = {
                            isSelectingLocation = false
                            locationName = ""
                            locationLat = null
                            locationLong = null
                        },
                        onAdd = { newName, newLat, newLong ->
                            locationName = newName
                            locationLat = newLat
                            locationLong = newLong
                            isSelectingLocation = false
                        }
                    )
                } else if (isAddingHashtags) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp)
                            .imePadding(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                OutlinedTextField(
                                    value = hashtagText,
                                    onValueChange = {
                                        hashtagText = it
                                    },
                                    label = { Text("Add hashtags", fontFamily = Ubuntu, fontWeight = FontWeight.Normal) },
                                    colors = TextFieldDefaults.colors(
                                        focusedContainerColor = Color.White,
                                        focusedTextColor = Color.Black,
                                        focusedLabelColor = Color.DarkGray,
                                        unfocusedContainerColor = Color.White,
                                        unfocusedTextColor = Color.Black,
                                        unfocusedLabelColor = Color.DarkGray
                                    )
                                )

                                IconButton(
                                    onClick = {
                                        hashtags = hashtags + hashtagText
                                        hashtagText = ""
                                    },
                                    colors = IconButtonDefaults.iconButtonColors(containerColor = Color(0xFFF10E91))
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Tag,
                                        contentDescription = "Add hashtag",
                                        tint = Color(0xFFFFD5ED)
                                    )
                                }
                            }
                        }

                        Box(
                            modifier = Modifier.weight(1f)
                        ) {
                            LazyColumn {
                                items(hashtags) { hashtag ->
                                    Box(
                                        modifier = Modifier.padding(horizontal = 2.dp)
                                    ) {
                                        HashtagButtonRemoveable(
                                            hashtag = hashtag,
                                            onRemove = { tagToRemove ->
                                                hashtags = hashtags - tagToRemove
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            TextButton(onClick = {
                                hashtags = emptyList()
                                hashtagText = ""
                                isAddingHashtags = false
                            }) {
                                Text(
                                    text = "Back",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontFamily = Ubuntu,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = Color(0xFFF10E91)
                                )
                            }

                            Button(
                                onClick = {
                                    hashtagText = ""
                                    isAddingHashtags = false
                                },
                                colors = buttonColors(containerColor = Color(0xFFF10E91))
                            ) {
                                Text(
                                    text = "Add",
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

    if (postsViewModel.showCreatePostSuccessToast) {
        Toast.makeText(LocalContext.current, "Successfully created post!", Toast.LENGTH_SHORT).show()
        postsViewModel.loadTotalPosts(userId, apiKey)
        postsViewModel.loadCompletePosts(userId, apiKey, 30, 1)
        postsViewModel.endShowCreatePostSuccessToast()
        onClose()
    }
}

@Composable
fun MainPostFormUI(
    onClose: () -> Unit,
    onSubmitPost: () -> Unit,
    postsViewModel: PostsViewModel,
    sessionViewModel: SessionViewModel,
    locationName: String,
    onAddLocationClick: () -> Unit,
    onAddHashtagsClick: () -> Unit,
    hashtags: List<String>,
    selectedImages: List<Uri>,
    onPhotoCaptured: (Uri) -> Unit,
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

                Box(
                    modifier = Modifier.weight(1f)
                ) {
                    OutlinedTextField(
                        value = postsViewModel.postText,
                        onValueChange = { postsViewModel.onPostTextChanged(it) },
                        label = { Text("What's on your mind?", fontFamily = Ubuntu, fontWeight = FontWeight.Normal) },
                        modifier = Modifier.fillMaxWidth(),
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

                Spacer(modifier = Modifier.height(8.dp))

                if (selectedImages.isNotEmpty() && !isKeyboardVisible()) {
                    AddImagesCarousel(
                        imageUrls = selectedImages,
                        heightDp = 250.dp
                    )
                }
                if (selectedImages.isNotEmpty() && isKeyboardVisible()) {
                    Text(
                        text = "Images hidden while typing...",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontFamily = Ubuntu,
                            fontWeight = FontWeight.Normal
                        ),
                        color = Color.DarkGray,
                        modifier = Modifier.align(Alignment.CenterHorizontally).padding(6.dp)
                    )
                }

                if (hashtags.isNotEmpty()) {
                    PostItemWithHashtags(
                        hashtags = hashtags
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CameraIconWithPermission(
                        onPhotoCaptured = { uri ->
                            onPhotoCaptured(uri)
                        }
                    )

                    IconButton(
                        onClick = { onPhotosAlbumClick() },
                        colors = IconButtonDefaults.iconButtonColors(containerColor = Color(0xFFF10E91)),
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.PhotoLibrary,
                            contentDescription = null,
                            tint = Color(0xFFFFD5ED)
                        )
                    }

                    IconButton(
                        onClick = { onAddHashtagsClick() },
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
                    onClick = { onSubmitPost() },
                    colors = buttonColors(containerColor = Color(0xFFF10E91))
                ) {
                    Text("Create", fontFamily = Ubuntu, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun CameraIconWithPermission(
    onPhotoCaptured: (Uri) -> Unit
) {
    val context = LocalContext.current

    var cameraPermissionGranted by remember { mutableStateOf(false) }

    var tempUri by remember { mutableStateOf<Uri?>(null) }

    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && tempUri != null) {
            onPhotoCaptured(tempUri!!)
        }
    }

    val requestCameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        cameraPermissionGranted = granted
        if (granted && tempUri != null) {
            takePictureLauncher.launch(tempUri!!)
        }
    }

    LaunchedEffect(Unit) {
        val hasPerm = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        cameraPermissionGranted = hasPerm
    }

    IconButton(
        onClick = {
            val uri = createImageFileUri(context)
            if (uri == null) return@IconButton

            tempUri = uri
            if (cameraPermissionGranted) {
                takePictureLauncher.launch(uri)
            } else {
                requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        },
        colors = IconButtonDefaults.iconButtonColors(containerColor = Color(0xFFF10E91)),
        modifier = Modifier.size(40.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.CameraAlt,
            contentDescription = null,
            tint = Color(0xFFFFD5ED)
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AddImagesCarousel(
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
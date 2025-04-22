package io.winapps.voizy.ui.features.people.person

import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AddLocation
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Poll
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import io.winapps.voizy.SessionViewModel
import io.winapps.voizy.ui.features.profile.AddImagesCarousel
import io.winapps.voizy.ui.features.profile.CameraIconWithPermission
import io.winapps.voizy.ui.features.profile.HashtagButtonRemoveable
import io.winapps.voizy.ui.features.profile.LocationPickerUI
import io.winapps.voizy.ui.features.profile.PostItem
import io.winapps.voizy.ui.features.profile.PostItemWithHashtags
import io.winapps.voizy.ui.features.profile.PostsViewModel
import io.winapps.voizy.ui.features.profile.isKeyboardVisible
import io.winapps.voizy.ui.theme.Ubuntu

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PersonPostsContent(
    onSelectViewPostComments: (Long) -> Unit,
    primaryAccent: Color,
    secondaryColor: Color,
    secondaryAccent: Color
) {
    val sessionViewModel = hiltViewModel<SessionViewModel>()
    val userId = sessionViewModel.userId ?: -1
    val apiKey = sessionViewModel.getApiKey().orEmpty()
    val token = sessionViewModel.getToken().orEmpty()

    val personViewModel = hiltViewModel<PersonViewModel>()
    val personId = personViewModel.selectedPersonId
    val preferredName = personViewModel.preferredName

    val postsViewModel = hiltViewModel<PersonPostsViewModel>()
    val posts = postsViewModel.completePosts
    val isLoading = postsViewModel.isLoadingPosts
    val errorMessage = postsViewModel.postsErrorMessage

    LaunchedEffect(Unit) {
        postsViewModel.loadCompletePosts(
            personId = personId,
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
                text = "Post on $preferredName's page",
                fontFamily = Ubuntu,
                fontWeight = FontWeight.Bold,
                color = secondaryAccent
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Send,
                contentDescription = "Post on $preferredName's page button",
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
                            text = "$preferredName hasn't posted anything yet",
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PostToPersonDialog(
    onClose: () -> Unit,
    postsViewModel: PostsViewModel,
//    personPostsViewModel: PersonPostsViewModel,
    personViewModel: PersonViewModel,
    sessionViewModel: SessionViewModel,
    personId: Long,
    primaryAccent: Color,
    secondaryColor: Color,
    secondaryAccent: Color
) {
    val context = LocalContext.current
    val userId = sessionViewModel.userId ?: -1
    val apiKey = sessionViewModel.getApiKey().orEmpty()
    val token = sessionViewModel.getToken().orEmpty()
    val username = sessionViewModel.username.orEmpty()
    val preferredName = sessionViewModel.preferredName.orEmpty()
    val selectedImages = personViewModel.selectedImages
    var isSelectingLocation by remember { mutableStateOf(false) }
    var isAddingHashtags by remember { mutableStateOf(false) }
    var locationName by remember { mutableStateOf("") }
    var locationLat by remember { mutableStateOf<Double?>(null) }
    var locationLong by remember { mutableStateOf<Double?>(null) }
    var selectedHashtags by remember { mutableStateOf<List<String>>(emptyList<String>()) }
    var hashtags by remember { mutableStateOf<List<String>>(emptyList<String>()) }
    var hashtagText by remember { mutableStateOf("") }

    val pickImagesLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents(),
        onResult = { uris ->
            val newList = if (uris.size > 10) uris.take(10) else uris
            personViewModel.addImages(
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
                .border(2.dp, secondaryColor, RoundedCornerShape(12.dp)),
            colors = CardDefaults.cardColors(containerColor = primaryAccent)
        ) {
            if (!isSelectingLocation && !isAddingHashtags) {
                PostToPersonMainUI(
                    onClose = { onClose() },
                    onSubmitPost = {
                        val locName = if (locationName.isBlank() || locationName.isEmpty()) null else locationName
                        personViewModel.submitPost(
                            personId = personId,
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
                    personViewModel = personViewModel,
//                    personPostsViewModel = personPostsViewModel,
                    postsViewModel = postsViewModel,
                    locationName = locationName,
                    onAddLocationClick = {
                        isAddingHashtags = false
                        isSelectingLocation = true
                    },
                    onAddHashtagsClick = {
                        isSelectingLocation = false
                        isAddingHashtags = true
                    },
                    hashtags = selectedHashtags,
                    selectedImages = selectedImages,
                    onRemoveImage = { uri ->
                        personViewModel.removeImage(uri)
                    },
                    onPhotoCaptured = { uri ->
                        personViewModel.addImage(uri)
                    },
                    onPhotosAlbumClick = {
                        pickImagesLauncher.launch("image/*")
                    },
                    secondaryColor = secondaryColor,
                    secondaryAccent = secondaryAccent
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
                        },
                        secondaryColor = secondaryColor,
                        secondaryAccent = secondaryAccent
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
                                    colors = IconButtonDefaults.iconButtonColors(containerColor = secondaryColor)
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Tag,
                                        contentDescription = "Add hashtag",
                                        tint = secondaryAccent
                                    )
                                }
                            }
                        }

                        Box(
                            modifier = Modifier.weight(1f)
                        ) {
                            FlowRow(
                                modifier = Modifier.fillMaxWidth().padding(2.dp)
                            ) {
                                hashtags.forEach { hashtag ->
                                    Box(
                                        modifier = Modifier.padding(horizontal = 2.dp)
                                    ) {
                                        HashtagButtonRemoveable(
                                            hashtag = hashtag,
                                            onRemove = { tagToRemove ->
                                                hashtags = hashtags - tagToRemove
                                            },
                                            secondaryColor = secondaryColor,
                                            secondaryAccent = secondaryAccent
                                        )
                                    }
                                }
                            }
                            FlowRow(
                                modifier = Modifier.fillMaxWidth().padding(2.dp)
                            ) {
                                selectedHashtags.forEach { selectedHashtag ->
                                    Box(
                                        modifier = Modifier.padding(horizontal = 2.dp)
                                    ) {
                                        HashtagButtonRemoveable(
                                            hashtag = selectedHashtag,
                                            onRemove = { tagToRemove ->
                                                selectedHashtags = selectedHashtags - tagToRemove
                                            },
                                            secondaryColor = secondaryColor,
                                            secondaryAccent = secondaryAccent
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
                                    color = secondaryColor
                                )
                            }

                            Button(
                                onClick = {
                                    if (hashtags.isNotEmpty()) {
                                        hashtags.forEach { newTag ->
                                            selectedHashtags = selectedHashtags + newTag
                                        }
                                    }
                                    hashtagText = ""
                                    isAddingHashtags = false
                                },
                                colors = buttonColors(containerColor = secondaryColor)
                            ) {
                                Text(
                                    text = "Add",
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

    if (personViewModel.showCreatePostSuccessToast) {
        Toast.makeText(LocalContext.current, "Successfully posted to $preferredName's page!", Toast.LENGTH_SHORT).show()
        personViewModel.loadTotalPosts(personId, userId, apiKey)
        personViewModel.loadCompletePosts(personId, userId, apiKey, 30, 1)
        personViewModel.endShowCreatePostSuccessToast()
        onClose()
    }
}

@Composable
fun PostToPersonMainUI(
    onClose: () -> Unit,
    onSubmitPost: () -> Unit,
    personViewModel: PersonViewModel,
//    personPostsViewModel: PersonPostsViewModel,
    postsViewModel: PostsViewModel,
    locationName: String,
    onAddLocationClick: () -> Unit,
    onAddHashtagsClick: () -> Unit,
    hashtags: List<String>,
    selectedImages: List<Uri>,
    onRemoveImage: (Uri) -> Unit,
    onPhotoCaptured: (Uri) -> Unit,
    onPhotosAlbumClick: () -> Unit,
    secondaryColor: Color,
    secondaryAccent: Color
) {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Post to ${personViewModel.preferredName}'s Page",
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
                .border(2.dp, secondaryColor, RoundedCornerShape(12.dp)),
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
                            } else {
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(
                                    imageVector = Icons.Filled.AddLocation,
                                    contentDescription = null,
                                    tint = secondaryColor,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Add location",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontFamily = Ubuntu,
                                        fontWeight = FontWeight.Normal
                                    ),
                                    color = secondaryColor,
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
                        value = personViewModel.postText,
                        onValueChange = { personViewModel.onPostTextChanged(it) },
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
                        onRemove = { uri ->
                            onRemoveImage(uri)
                        },
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
                        hashtags = hashtags,
                        secondaryColor = secondaryColor,
                        secondaryAccent = secondaryAccent
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
                        },
                        secondaryColor = secondaryColor,
                        secondaryAccent = secondaryAccent
                    )

                    IconButton(
                        onClick = { onPhotosAlbumClick() },
                        colors = IconButtonDefaults.iconButtonColors(containerColor = secondaryColor),
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.PhotoLibrary,
                            contentDescription = null,
                            tint = secondaryAccent
                        )
                    }

                    IconButton(
                        onClick = { onAddHashtagsClick() },
                        colors = IconButtonDefaults.iconButtonColors(containerColor = secondaryColor),
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Tag,
                            contentDescription = null,
                            tint = secondaryAccent
                        )
                    }

                    IconButton(
                        onClick = {},
                        colors = IconButtonDefaults.iconButtonColors(containerColor = Color.Gray),
//                        colors = IconButtonDefaults.iconButtonColors(containerColor = Color(0xFFF10E91)),
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Groups,
                            contentDescription = null,
                            tint = secondaryAccent
                        )
                    }

                    IconButton(
                        onClick = {},
                        colors = IconButtonDefaults.iconButtonColors(containerColor = Color.Gray),
//                        colors = IconButtonDefaults.iconButtonColors(containerColor = Color(0xFFF10E91)),
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Poll,
                            contentDescription = null,
                            tint = secondaryAccent
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
                    color = secondaryColor,
                    fontFamily = Ubuntu,
                    fontWeight = FontWeight.Bold
                )
            }

            if (personViewModel.isSubmittingPost) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = secondaryColor
                )
            } else {
                Button(
                    onClick = { onSubmitPost() },
                    colors = buttonColors(containerColor = secondaryColor)
                ) {
                    Text("Create", fontFamily = Ubuntu, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
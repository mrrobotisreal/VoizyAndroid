package io.winapps.voizy.ui.features.profile

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddToPhotos
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonPin
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import io.winapps.voizy.SessionViewModel
import io.winapps.voizy.data.model.users.UserImage
import io.winapps.voizy.ui.theme.Ubuntu

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PhotosContent(
    images: List<UserImage>,
    setFullScreenImageOpen: (Boolean) -> Unit,
    setCurrentImageIndex: (Int) -> Unit,
    secondaryColor: Color,
    secondaryAccent: Color
) {
    val photosViewModel = hiltViewModel<PhotosViewModel>()

    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            Button(
                onClick = { photosViewModel.onOpenAddImages() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 8.dp, vertical = 2.dp
                    ),
                colors = ButtonDefaults.buttonColors(containerColor = secondaryColor)
            ) {
                Text(
                    text = "Add images",
                    fontFamily = Ubuntu,
                    fontWeight = FontWeight.Bold,
                    color = secondaryAccent
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Filled.AddToPhotos,
                    contentDescription = "Add images button",
                )
            }

            Card(
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(2.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier.fillMaxSize().padding(4.dp)
                ) {
                    items(images.size) { index ->
                        val image = images[index]
                        PhotoGridItem(
                            imageUrl = image.imageURL,
                            onClick = {
                                setCurrentImageIndex(index)
                                setFullScreenImageOpen(true)
                            },
                            secondaryColor = secondaryColor
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PhotoGridItem(
    imageUrl: String,
    onClick: () -> Unit,
    secondaryColor: Color
) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clickable { onClick() }
            .padding(2.dp)
    ) {
        val painter = rememberAsyncImagePainter(
            model = imageUrl
        )

        Image(
            painter = painter,
            contentDescription = "User image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        if (painter.state is AsyncImagePainter.State.Loading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = secondaryColor
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FullScreenImageViewer(
    images: List<UserImage>,
    startIndex: Int,
    onClose: () -> Unit,
    secondaryColor: Color,
    secondaryAccent: Color
) {
    val context = LocalContext.current
    val photosViewModel = hiltViewModel<PhotosViewModel>()
    val sessionViewModel = hiltViewModel<SessionViewModel>()
    val profileViewModel = hiltViewModel<ProfileViewModel>()
    val userId = sessionViewModel.userId ?: 0
    val apiKey = sessionViewModel.getApiKey() ?: ""
    val token = sessionViewModel.getToken() ?: ""
    var currentImageId by remember { mutableLongStateOf(0) }
    val pagerState = rememberPagerState(
        initialPage = startIndex
    )
    var isUiVisible by remember { mutableStateOf(true) }
    val modifier = if (isUiVisible) {
        Modifier
            .fillMaxSize()
            .background(Color.Black)
            .systemBarsPadding()
    } else {
        Modifier
            .fillMaxSize()
            .background(Color.Black)
    }

    Box(
        modifier = modifier
    ) {
        HorizontalPager(
            count = images.size,
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            isUiVisible = !isUiVisible
                        }
                    )
                }
        ) { page ->
            val image = images[page]
            currentImageId = image.imageID
            val painter = rememberAsyncImagePainter(
                model = image.imageURL
            )

            Image(
                painter = painter,
                contentDescription = "User image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )

            if (painter.state is AsyncImagePainter.State.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = secondaryColor
                )
            }
        }

        if (isUiVisible) {
            IconButton(
                onClick = { onClose() },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = Color.White
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(8.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (photosViewModel.isUpdatingProfilePic) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            color = secondaryColor
                        )
                    } else {
                        Button(
                            onClick = {
                                photosViewModel.updateProfilePic(
                                    userId = userId,
                                    imageId = currentImageId,
                                    apiKey = apiKey,
                                    token = token
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    horizontal = 8.dp, vertical = 2.dp
                                ),
                            colors = ButtonDefaults.buttonColors(containerColor = secondaryColor)
                        ) {
                            Text(
                                text = "Set as Profile Pic",
                                fontFamily = Ubuntu,
                                fontWeight = FontWeight.Bold,
                                color = secondaryAccent
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                imageVector = Icons.Filled.PersonPin,
                                contentDescription = "Set as Profile Pic button",
                            )
                        }
                    }

                    if (photosViewModel.isUpdatingCoverPic) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            color = secondaryColor
                        )
                    } else {
                        Button(
                            onClick = {
                                photosViewModel.updateCoverPic(
                                    userId = userId,
                                    imageId = currentImageId,
                                    apiKey = apiKey,
                                    token = token
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    horizontal = 8.dp, vertical = 2.dp
                                ),
                            colors = ButtonDefaults.buttonColors(containerColor = secondaryColor)
                        ) {
                            Text(
                                text = "Set as Cover Pic",
                                fontFamily = Ubuntu,
                                fontWeight = FontWeight.Bold,
                                color = secondaryAccent
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                imageVector = Icons.Filled.Photo,
                                contentDescription = "Set as Cover Pic button",
                            )
                        }
                    }
                }
            }
        }
    }

    if (photosViewModel.showUpdateProfilePicSuccessToast) {
        Toast.makeText(context, "Successfully updated profile picture!", Toast.LENGTH_SHORT).show()
        profileViewModel.loadProfilePic(userId, apiKey)
        photosViewModel.endShowUpdateProfilePicSuccessToast()
    }

    if (photosViewModel.showUpdateCoverPicSuccessToast) {
        Toast.makeText(context, "Successfully updated cover picture!", Toast.LENGTH_SHORT).show()
        profileViewModel.loadCoverPic(userId, apiKey)
        photosViewModel.endShowUpdateCoverPicSuccessToast()
    }
}

@Composable
fun CameraButtonWithPermission(
    onPhotoCaptured: (Uri) -> Unit,
    secondaryColor: Color,
    secondaryAccent: Color
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

    Button(
        onClick = {
            val uri = createImageFileUri(context)
            if (uri == null) return@Button

            tempUri = uri
            if (cameraPermissionGranted) {
                takePictureLauncher.launch(uri)
            } else {
                requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 8.dp, vertical = 2.dp
            ),
        colors = ButtonDefaults.buttonColors(containerColor = secondaryColor)
    ) {
        Text(
            text = "Take A New Photo",
            fontFamily = Ubuntu,
            fontWeight = FontWeight.Bold,
            color = secondaryAccent
        )
        Spacer(modifier = Modifier.width(8.dp))
        Icon(
            imageVector = Icons.Filled.CameraAlt,
            contentDescription = "Take A New Photo button",
        )
    }
}

@Composable
fun PhotosButtonWithPermission(
    onPhotosSelected: (List<Uri>) -> Unit,
    secondaryColor: Color,
    secondaryAccent: Color
) {
    val pickImagesLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents(),
        onResult = { uris ->
            val newList = if (uris.size > 10) uris.take(10) else uris
            onPhotosSelected(
                newList
            )
        }
    )

    Button(
        onClick = { pickImagesLauncher.launch("image/*") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 8.dp, vertical = 2.dp
            ),
        colors = ButtonDefaults.buttonColors(containerColor = secondaryColor)
    ) {
        Text(
            text = "Choose From Photos",
            fontFamily = Ubuntu,
            fontWeight = FontWeight.Bold,
            color = secondaryAccent
        )
        Spacer(modifier = Modifier.width(8.dp))
        Icon(
            imageVector = Icons.Filled.AddToPhotos,
            contentDescription = "Choose From Photos button",
        )
    }
}
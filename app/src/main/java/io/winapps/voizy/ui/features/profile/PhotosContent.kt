package io.winapps.voizy.ui.features.profile

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import io.winapps.voizy.SessionViewModel
import io.winapps.voizy.data.model.users.UserImage

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PhotosContent(
    images: List<UserImage>,
//    isFullScreenImageOpen: Boolean,
    setFullScreenImageOpen: (Boolean) -> Unit,
//    currentImageIndex: Int,
    setCurrentImageIndex: (Int) -> Unit,
) {
//    val photosViewModel = hiltViewModel<PhotosViewModel>()
//    val sessionViewModel = hiltViewModel<SessionViewModel>()
//    val images = photosViewModel.userImages
//    var isFullScreenOpen by remember { mutableStateOf(false) }
//    var selectedIndex by remember { mutableIntStateOf(0) }

//    LaunchedEffect(Unit) {
//        val userId = sessionViewModel.userId ?: 0
//        val apiKey = sessionViewModel.getApiKey().orEmpty()
//        photosViewModel.loadUserImages(
//            userId = userId,
//            apiKey = apiKey,
//            limit = 40,
//            page = 1,
//        )
//    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.fillMaxSize()
        ) {
            items(images.size) { index ->
                val image = images[index]
                PhotoGridItem(
                    imageUrl = image.imageURL,
                    onClick = {
                        setCurrentImageIndex(index)
                        setFullScreenImageOpen(true)
                    }
                )
            }
        }
    }
}

@Composable
fun PhotoGridItem(
    imageUrl: String,
    onClick: () -> Unit
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
                color = Color(0xFFF10E91)
            )
        }

//        AsyncImage(
//            model = imageUrl,
//            contentDescription = "User image",
//            modifier = Modifier
//                .fillMaxSize()
//                .background(Color.LightGray),
//            contentScale = ContentScale.Crop
//        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FullScreenImageViewer(
    images: List<UserImage>,
    startIndex: Int,
    onClose: () -> Unit
) {
    val pagerState = rememberPagerState(
        initialPage = startIndex
    )
    var isUiVisible by remember { mutableStateOf(true) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
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
                    color = Color(0xFFF10E91)
                )
            }

//            AsyncImage(
//                model = image.imageURL,
//                contentDescription = "Full-screen image",
//                modifier = Modifier
//                    .fillMaxSize(),
//                contentScale = ContentScale.Fit
//            )
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
        }
    }
}
package io.winapps.voizy.ui.features.people.person

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import io.winapps.voizy.data.model.users.UserImage
import io.winapps.voizy.ui.features.profile.PhotoGridItem

@Composable
fun PersonPhotosContent(
    images: List<UserImage>,
    setFullScreenImageOpen: (Boolean) -> Unit,
    setCurrentImageIndex: (Int) -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column {
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
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FullScreenPersonImageViewer(
    images: List<UserImage>,
    startIndex: Int,
    onClose: () -> Unit
) {
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
                    color = Color(0xFFF10E91)
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
                    // TODO: Add ability to comment on images and react to images
                }
            }
        }
    }
}
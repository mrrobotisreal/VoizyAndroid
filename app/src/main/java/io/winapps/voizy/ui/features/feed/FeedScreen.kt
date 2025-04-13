package io.winapps.voizy.ui.features.feed

import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import io.winapps.voizy.R
import io.winapps.voizy.data.model.users.UserImage
import io.winapps.voizy.ui.features.profile.FullScreenImageViewer
import io.winapps.voizy.ui.features.profile.PostsContent
import io.winapps.voizy.ui.navigation.BottomNavBar
import io.winapps.voizy.ui.theme.Ubuntu

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FeedScreen() {
    val feedViewModel = hiltViewModel<FeedViewModel>()
    val view = LocalView.current
    val searchText = feedViewModel.searchText
    val showFiltersDialog = feedViewModel.showFiltersDialog
    val selectedFilter = feedViewModel.selectedFilter
    val selectedFilterLabel = feedViewModel.selectedFilterLabel
    var isViewingComments by remember { mutableStateOf(false) }
    var selectedPostID by remember { mutableLongStateOf(0) }
    var images by remember { mutableStateOf(emptyList<UserImage>()) }
    val (isFullScreenImageOpen, setFullScreenImageOpen) = rememberSaveable { mutableStateOf(false) }
    val (currentImageIndex, setCurrentImageIndex) = rememberSaveable { mutableIntStateOf(0) }

    SideEffect {
        val activity = view.context as Activity
        activity.window.statusBarColor = Color(0xFFF9D841).toArgb()
        WindowCompat.getInsetsController(activity.window, view).isAppearanceLightStatusBars = true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFDF4C9))
            .statusBarsPadding()
            .imePadding()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF9D841))
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.padding(4.dp)
            ) {
                Box(
                    modifier = Modifier.padding(end = 4.dp)
                ) {
                    Card(
                        elevation = CardDefaults.cardElevation(12.dp),
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .border(1.dp, Color(0xFFF10E91), RoundedCornerShape(12.dp))
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.voizy_full_square),
                            contentDescription = "Voizy full logo",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.padding(4.dp)
                ) {
                    OutlinedTextField(
                        value = searchText,
                        onValueChange = {
                            feedViewModel.onSearchTextChanged(it)
                        },
                        label = {
                            Text(
                                "Search $selectedFilterLabel",
                                fontFamily = Ubuntu,
                                fontWeight = FontWeight.Normal
                            )
                                },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Search,
                                contentDescription = "Search",
                                tint = Color(0xFFF10E91)
                            )
                        },
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
                        onClick = { feedViewModel.onOpenFiltersDialog() },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = Color(0xFFF10E91)
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Filled.FilterAlt,
                            contentDescription = "Filters",
                            tint = Color(0xFFFFD5ED)
                        )
                    }
                }
            }
        }

        Divider(
            color = Color(0xFFF10E91),
            modifier = Modifier
                .height(1.dp)
                .fillMaxWidth()
        )

        FeedTabsRow(
            selectedTab = selectedFilter,
            onTabSelected = { tab ->
                feedViewModel.onSelectFilter(tab)
            }
        )

        Box(
            modifier = Modifier.weight(1f)
        ) {
            when (selectedFilter) {
                FeedTab.FOR_YOU -> PostsContent(
                    onSelectViewPostComments = { postID ->
                        selectedPostID = postID
                        isViewingComments = true
                    }
                )
                FeedTab.POPULAR -> PostsContent(
                    onSelectViewPostComments = { postID ->
                        selectedPostID = postID
                        isViewingComments = true
                    }
                )
                FeedTab.GROUPS -> PostsContent(
                    onSelectViewPostComments = { postID ->
                        selectedPostID = postID
                        isViewingComments = true
                    }
                )
                FeedTab.FRIENDS -> PostsContent(
                    onSelectViewPostComments = { postID ->
                        selectedPostID = postID
                        isViewingComments = true
                    }
                )
            }
        }

        BottomNavBar()
    }

    if (showFiltersDialog) {
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
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally)
                        .padding(8.dp)
                ) {
                    Text(
                        text = "Filters",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontFamily = Ubuntu,
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color.Black,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                FiltersDropdown(
                    selectedFilter = selectedFilter,
                    onSelectFilter = { filter ->
                        feedViewModel.onSelectFilter(filter)
                    }
                )

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(4.dp).fillMaxWidth()
                ) {
                    TextButton(
                        onClick = {
                            feedViewModel.onCloseFiltersDialog()
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

                    Button(
                        onClick = {
                            feedViewModel.onCloseFiltersDialog()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF10E91))
                    ) {
                        Text(
                            text = "Apply filter",
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
                }
            )
        }
    }
}

@Composable
fun FiltersDropdown(
    selectedFilter: FeedTab,
    onSelectFilter: (FeedTab) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
    ) {
        Row(
            modifier = Modifier
                .clickable { expanded = true }
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = selectedFilter.label,
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(end = 8.dp)
            )

            Icon(
                imageVector = if (expanded) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                contentDescription = "Dropdown Arrow",
                tint = Color(0xFFF10E91)
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(IntrinsicSize.Min)
                .background(Color(0xFFFDF4C9))
        ) {
            FeedTab.entries.forEach { option ->
                DropdownMenuItem(
                    onClick = {
                        onSelectFilter(option)
                        expanded = false
                    },
                    text = { Text(option.label, fontFamily = Ubuntu, fontWeight = FontWeight.Normal) },
                    leadingIcon = {
                        if (option == selectedFilter) {
                            Icon(
                                imageVector = Icons.Rounded.Check,
                                contentDescription = "Selected",
                                tint = Color.Green
                            )
                        }
                    }
                )
            }
        }
    }
}
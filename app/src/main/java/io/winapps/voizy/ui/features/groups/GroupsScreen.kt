package io.winapps.voizy.ui.features.groups

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import coil.size.Size
import io.winapps.voizy.data.model.users.Group
import io.winapps.voizy.ui.navigation.BottomNavBar
import io.winapps.voizy.ui.theme.Ubuntu

@Composable
fun GroupsScreen() {
    val groupsViewModel = hiltViewModel<GroupsViewModel>()
    val view = LocalView.current
    val searchText = groupsViewModel.searchText

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
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.padding(4.dp).fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = searchText,
                    onValueChange = {
                        groupsViewModel.onSearchTextChanged(it)
                    },
                    label = {
                        Text(
                            "Search for Groups",
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
            }
        }

        Divider(
            color = Color(0xFFF10E91),
            modifier = Modifier
                .height(1.dp)
                .fillMaxWidth()
        )

        TopicsRow(
            selectedTopic = groupsViewModel.selectedTopic,
            onTopicSelected = { topic ->
                groupsViewModel.onSelectTopic(topic)
            }
        )

        Box(
            modifier = Modifier.weight(1f).padding(4.dp)
        ) {
            if (groupsViewModel.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = Color(0xFFF10E91)
                    )
                }
            } else if (groupsViewModel.groups.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Groups,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = Color(0xFFF10E91)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "No groups to display. Groups feature will be available soon!",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontFamily = Ubuntu,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            } else {
                GroupsContent(
                    groups = groupsViewModel.groups,
                    onGroupClick = {},
                    isLoading = groupsViewModel.isLoading
                )
            }
        }

        BottomNavBar()
    }
}

@Composable
fun GroupsContent(
    groups: List<Group>,
    onGroupClick: (Group) -> Unit,
    isLoading: Boolean = false
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        val rows = groups.chunked(2)

        items(rows) { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                rowItems.forEach { group ->
                    GroupCard(
                        group = group,
                        onClick = onGroupClick,
                        modifier = Modifier.weight(1f)
                    )
                }

                if (rowItems.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun GroupCard(
    group: Group,
    onClick: (Group) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .border(1.dp, Color(0xFFF10E91), RoundedCornerShape(12.dp))
            .aspectRatio(0.8f)
            .clickable { onClick(group) },
        elevation = CardDefaults.cardElevation(7.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.background(Color.White)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(Color(0xFFFFD5ED)),
                contentAlignment = Alignment.Center
            ) {
                if (group.imageURL != null) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(group.imageURL)
                            .crossfade(true)
                            .scale(Scale.FILL)
                            .size(Size.ORIGINAL)
                            .build(),
                        contentDescription = "Image",
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFFFFD5ED)),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = Icons.Filled.Groups,
                        contentDescription = "Group",
                        modifier = Modifier.size(48.dp),
                        tint = Color(0xFFF10E91)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(12.dp)
                    .background(Color.White),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = group.name,
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontFamily = Ubuntu,
                        fontWeight = FontWeight.Bold
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = formatMemberCount(group.totalUsers),
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

fun formatMemberCount(count: Long): String {
    return when {
        count < 1000 -> "$count members"
        count < 10000 -> String.format("%.1fk members", count / 1000.0).replace(".0k", "k")
        count < 1000000 -> "${count / 1000}k members"
        else -> String.format("%1fm members", count / 1000000.0).replace(".0m", "m")
    }
}
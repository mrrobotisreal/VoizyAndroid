package io.winapps.voizy.ui.home

import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import io.winapps.voizy.R
import io.winapps.voizy.ui.features.profile.PostsContent
import io.winapps.voizy.ui.navigation.BottomNavBar
import io.winapps.voizy.ui.theme.Ubuntu

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen() {
    val configuration = LocalConfiguration.current
    val horizontalPadding = if (configuration.screenWidthDp >= 600) 200.dp else 10.dp
    val homeViewModel = hiltViewModel<HomeViewModel>()
    val view = LocalView.current

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
//                                .border(1.dp, Color(0xFFF10E91), RoundedCornerShape(12.dp))
//                                .shadow(elevation = 12.dp, shape = RoundedCornerShape(12.dp))
                        )
                    }
                }

                OutlinedTextField(
                    value = homeViewModel.searchText,
                    onValueChange = {
                        homeViewModel.onSearchTextChanged(it)
                    },
                    label = { Text("Search Voizy...", fontFamily = Ubuntu, fontWeight = FontWeight.Normal) },
                    trailingIcon = {
                        IconButton(
                            onClick = {}
                        ) {
                            Icon(
                                Icons.Filled.Search,
                                contentDescription = "Search",
                                tint = Color(0xFFF10E91)
                            )
                        }
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

        Box(
            modifier = Modifier.weight(1f)
        ) {
            PostsContent(
                onSelectViewPostComments = { _ ->
                    //
                }
            )
        }

        BottomNavBar()
    }
}
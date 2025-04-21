package io.winapps.voizy.ui.features.more

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.winapps.voizy.SessionViewModel
import io.winapps.voizy.ui.theme.Ubuntu

@Composable
fun AppPreferencesDialog(
    onClose: () -> Unit
) {
    val sessionViewModel = hiltViewModel<SessionViewModel>()
    val userId = sessionViewModel.userId ?: -1
    val apiKey = sessionViewModel.getApiKey().orEmpty()
    val token = sessionViewModel.getToken().orEmpty()
    val moreViewModel = hiltViewModel<MoreViewModel>()
    val userPreferences = moreViewModel.userPreferences

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
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "App Preferences",
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
                        modifier = Modifier.padding(8.dp),
                        verticalArrangement = Arrangement.SpaceAround,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "App Theme",
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontFamily = Ubuntu,
                                        fontWeight = FontWeight.Bold
                                    )
                                )

                                Text(
                                    text = "This is where you choose how you want the entire app's color scheme to look. This is app-wide, but it is not visible by other users, only you.",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontFamily = Ubuntu,
                                        fontWeight = FontWeight.Normal
                                    )
                                )

                                TextButton(
                                    onClick = {
                                        moreViewModel.onShowAppThemes()
                                    },
                                    modifier = Modifier.padding(vertical = 2.dp, horizontal = 8.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD5ED)),
                                    border = BorderStroke(1.dp, Color(0xFFF10E91))
                                ) {
                                    Text(
                                        text = moreViewModel.selectedAppTheme.label,
                                        style = MaterialTheme.typography.bodyLarge.copy(
                                            fontFamily = Ubuntu,
                                            fontWeight = FontWeight.Bold
                                        ),
                                        color = Color(0xFFF10E91)
                                    )
                                }
                            }
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Song Autoplay",
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontFamily = Ubuntu,
                                        fontWeight = FontWeight.Bold
                                    )
                                )

                                Text(
                                    text = "This is where you choose whether or not you want the songs on other users' profiles to automatically begin playing when you visit their page or not. This is app-wide, but only applies to other users' profiles, not your own profile.",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontFamily = Ubuntu,
                                        fontWeight = FontWeight.Normal
                                    )
                                )

                                Switch(
                                    checked = moreViewModel.userPreferences.songAutoplay,
                                    onCheckedChange = {},
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = Color(0xFFF10E91),
                                        uncheckedThumbColor = Color.Gray,
                                        checkedTrackColor = Color(0xFFFFD5ED),
                                        uncheckedTrackColor = Color.LightGray
                                    )
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

                    if (moreViewModel.isUpdatingPrefs) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color(0xFFF10E91)
                        )
                    } else {
                        Button(
                            onClick = { moreViewModel.onUpdateAppPrefs(
                                apiKey = apiKey,
                                userId = userId,
                                token = token,
                                onClose = { onClose() }
                            ) },
                            colors = buttonColors(containerColor = Color(0xFFF10E91))
                        ) {
                            Text("Create", fontFamily = Ubuntu, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
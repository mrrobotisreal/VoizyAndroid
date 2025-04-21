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
fun ProfilePreferencesDialog(
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
                .border(2.dp, moreViewModel.appSecondaryColor, RoundedCornerShape(12.dp)),
            colors = CardDefaults.cardColors(containerColor = moreViewModel.appPrimaryAccent)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (moreViewModel.showProfileThemes) {
                    Text(
                        text = "Choose a Profile Theme",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.Black,
                        fontFamily = Ubuntu,
                        fontWeight = FontWeight.Bold
                    )

                    Card(
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(1.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .weight(1F)
                            .border(2.dp, moreViewModel.appSecondaryColor, RoundedCornerShape(12.dp)),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(
                            modifier = Modifier.padding(8.dp),
                            verticalArrangement = Arrangement.SpaceEvenly,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            val currentTheme = moreViewModel.selectedProfileTheme
                            TextButton(
                                onClick = {
                                    moreViewModel.onSelectProfileTheme(AppTheme.VOIZY)
                                },
                                modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp, horizontal = 8.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (currentTheme == AppTheme.VOIZY) moreViewModel.appSecondaryColor else moreViewModel.appSecondaryAccent
                                ),
                                border = BorderStroke(1.dp, if (currentTheme == AppTheme.VOIZY) moreViewModel.appSecondaryAccent else moreViewModel.appSecondaryColor)
                            ) {
                                Text(
                                    text = "Voizy",
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontFamily = Ubuntu,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = if (currentTheme == AppTheme.VOIZY) moreViewModel.appSecondaryAccent else moreViewModel.appSecondaryColor
                                )
                            }

                            TextButton(
                                onClick = {
                                    moreViewModel.onSelectProfileTheme(AppTheme.OCEANIC)
                                },
                                modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp, horizontal = 8.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (currentTheme == AppTheme.OCEANIC) moreViewModel.appSecondaryColor else moreViewModel.appSecondaryAccent
                                ),
                                border = BorderStroke(1.dp, if (currentTheme == AppTheme.OCEANIC) moreViewModel.appSecondaryAccent else moreViewModel.appSecondaryColor)
                            ) {
                                Text(
                                    text = "Oceanic",
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontFamily = Ubuntu,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = if (currentTheme == AppTheme.OCEANIC) moreViewModel.appSecondaryAccent else moreViewModel.appSecondaryColor
                                )
                            }

                            TextButton(
                                onClick = {
                                    moreViewModel.onSelectProfileTheme(AppTheme.ROYAL)
                                },
                                modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp, horizontal = 8.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (currentTheme == AppTheme.ROYAL) moreViewModel.appSecondaryColor else moreViewModel.appSecondaryAccent
                                ),
                                border = BorderStroke(1.dp, if (currentTheme == AppTheme.ROYAL) moreViewModel.appSecondaryAccent else moreViewModel.appSecondaryColor)
                            ) {
                                Text(
                                    text = "Royal",
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontFamily = Ubuntu,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = if (currentTheme == AppTheme.ROYAL) moreViewModel.appSecondaryAccent else moreViewModel.appSecondaryColor
                                )
                            }

                            TextButton(
                                onClick = {
                                    moreViewModel.onSelectProfileTheme(AppTheme.SUNSET)
                                },
                                modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp, horizontal = 8.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (currentTheme == AppTheme.SUNSET) moreViewModel.appSecondaryColor else moreViewModel.appSecondaryAccent
                                ),
                                border = BorderStroke(1.dp, if (currentTheme == AppTheme.SUNSET) moreViewModel.appSecondaryAccent else moreViewModel.appSecondaryColor)
                            ) {
                                Text(
                                    text = "Sunset",
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontFamily = Ubuntu,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = if (currentTheme == AppTheme.SUNSET) moreViewModel.appSecondaryAccent else moreViewModel.appSecondaryColor
                                )
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(onClick = { moreViewModel.onCloseProfileThemes() }) {
                            Text(
                                text = "Back",
                                color = moreViewModel.appSecondaryColor,
                                fontFamily = Ubuntu,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        if (moreViewModel.isUpdatingPrefs) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = moreViewModel.appSecondaryColor
                            )
                        } else {
                            Button(
                                onClick = {
                                    moreViewModel.onCloseProfileThemes()
                                    sessionViewModel.updateUserPrefs(moreViewModel.userPreferences)
                                },
                                colors = buttonColors(containerColor = moreViewModel.appSecondaryColor)
                            ) {
                                Text("Select Theme", fontFamily = Ubuntu, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                } else {
                    Text(
                        text = "Profile Preferences",
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
                            .border(2.dp, moreViewModel.appSecondaryColor, RoundedCornerShape(12.dp)),
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
                                        text = "Profile Theme",
                                        style = MaterialTheme.typography.titleLarge.copy(
                                            fontFamily = Ubuntu,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )

                                    Text(
                                        text = "This is where you choose how you want your profile's color scheme to look. This color scheme is what will be displayed when anyone visits your profile page, regardless of what their App Theme is set to. This is YOUR profile, so it's up to YOU to decide what other users will see when visiting your page.",
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontFamily = Ubuntu,
                                            fontWeight = FontWeight.Normal
                                        )
                                    )

                                    TextButton(
                                        onClick = {
                                            moreViewModel.onShowProfileThemes()
                                        },
                                        modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp, horizontal = 8.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = moreViewModel.appSecondaryAccent),
                                        border = BorderStroke(1.dp, moreViewModel.appSecondaryColor)
                                    ) {
                                        Text(
                                            text = moreViewModel.selectedProfileTheme.label,
                                            style = MaterialTheme.typography.bodyLarge.copy(
                                                fontFamily = Ubuntu,
                                                fontWeight = FontWeight.Bold
                                            ),
                                            color = moreViewModel.appSecondaryColor
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
                                        text = "Profile Song Autoplay",
                                        style = MaterialTheme.typography.titleLarge.copy(
                                            fontFamily = Ubuntu,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )

                                    Text(
                                        text = "This is where you choose whether or not you want the song on your profile to automatically begin playing when someone visits your page or not. Note: Yours and other users' Song Autoplay setting in App Preferences will override this, meaning that your profile song will only autoplay if their App Preferences Song Autoplay is ON.",
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontFamily = Ubuntu,
                                            fontWeight = FontWeight.Normal
                                        )
                                    )

                                    TextSwitch(
                                        checked = moreViewModel.profileSongAutoplay,
                                        onCheckedChange = {
                                            moreViewModel.onToggleProfileSongAutoplay(it)
                                            sessionViewModel.updateUserPrefs(moreViewModel.userPreferences)
                                        },
                                        useTextColor = true,
                                        onTextColor = moreViewModel.appSecondaryAccent,
                                        offTextColor = Color.DarkGray
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
                                color = moreViewModel.appSecondaryColor,
                                fontFamily = Ubuntu,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        if (moreViewModel.isUpdatingPrefs) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = moreViewModel.appSecondaryColor
                            )
                        } else {
                            Button(
                                onClick = { moreViewModel.onUpdateProfilePrefs(
                                    apiKey = apiKey,
                                    userId = userId,
                                    token = token,
                                    onClose = { onClose() }
                                ) },
                                colors = buttonColors(containerColor = moreViewModel.appSecondaryColor)
                            ) {
                                Text("Save", fontFamily = Ubuntu, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}
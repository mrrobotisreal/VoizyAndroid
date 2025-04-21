package io.winapps.voizy.ui.features.more

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
                .border(2.dp, moreViewModel.appSecondaryColor, RoundedCornerShape(12.dp)),
            colors = CardDefaults.cardColors(containerColor = moreViewModel.appPrimaryAccent)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (moreViewModel.showAppThemes) {
                    Text(
                        text = "Choose an App Theme",
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
                            val currentTheme = moreViewModel.selectedAppTheme
                            TextButton(
                                onClick = {
                                    moreViewModel.onSelectAppTheme(AppTheme.VOIZY)
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
                                    moreViewModel.onSelectAppTheme(AppTheme.OCEANIC)
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
                                    moreViewModel.onSelectAppTheme(AppTheme.ROYAL)
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
                                    moreViewModel.onSelectAppTheme(AppTheme.SUNSET)
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
                        TextButton(onClick = { moreViewModel.onCloseAppThemes() }) {
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
                                    moreViewModel.onCloseAppThemes()
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
                                        modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp, horizontal = 8.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = moreViewModel.appSecondaryAccent),
                                        border = BorderStroke(1.dp, moreViewModel.appSecondaryColor)
                                    ) {
                                        Text(
                                            text = moreViewModel.selectedAppTheme.label,
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

                                    TextSwitch(
                                        checked = moreViewModel.songAutoplay,
                                        onCheckedChange = {
                                            moreViewModel.onToggleSongAutoplay(it)
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
                                onClick = { moreViewModel.onUpdateAppPrefs(
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

@Composable
fun TextSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    width: Dp = 80.dp,
    height: Dp = 40.dp,
    onColor: Color = Color(0xFFF10E91),
    offColor: Color = Color.Gray,
    thumbColor: Color = Color.White,
    onThumbColor: Color = Color(0xFFF10E91),
    offThumbColor: Color = Color.White,
    onTrackColor: Color = Color(0xFFFFD5ED),
    offTrackColor: Color = Color.Gray,
    useTextColor: Boolean = false,
    onTextColor: Color? = null,
    offTextColor: Color? = null,
    textStyle: TextStyle = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold),
    animationDuration: Int = 250,
    padding: Dp = 4.dp
) {
    val thumbSize = height - padding * 2
    val maxOffset  = width - thumbSize - padding * 2

    val thumbOffset by animateDpAsState(
        targetValue   = if (checked) maxOffset else 0.dp,
        animationSpec = tween(animationDuration)
    )

    Box(
        modifier = Modifier
            .size(width, height)
            .clip(RoundedCornerShape(height / 2))
            .background(if (checked) onTrackColor else offTrackColor)
            .clickable { onCheckedChange(!checked) }
    ) {
        Box(
            modifier = Modifier
                .offset(x = thumbOffset)
                .padding(padding)
                .size(thumbSize)
                .background(if (checked) onThumbColor else offThumbColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            if (useTextColor && onTextColor != null && offTextColor != null) {
                if (checked) {
                    Text(
                        text      = "ON",
                        style     = textStyle,
                        textAlign = TextAlign.Center,
                        color = onTextColor
                    )
                } else {
                    Text(
                        text      = "OFF",
                        style     = textStyle,
                        textAlign = TextAlign.Center,
                        color = offTextColor
                    )
                }
            } else {
                Text(
                    text      = if (checked) "ON" else "OFF",
                    style     = textStyle,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
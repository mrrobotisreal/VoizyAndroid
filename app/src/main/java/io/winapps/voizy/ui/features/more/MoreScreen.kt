package io.winapps.voizy.ui.features.more

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AppSettingsAlt
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import io.winapps.voizy.SessionViewModel
import io.winapps.voizy.ui.navigation.BottomNavBar
import io.winapps.voizy.ui.theme.Ubuntu

@Composable
fun MoreScreen() {
    val view = LocalView.current
    val sessionViewModel = hiltViewModel<SessionViewModel>()
    val primaryColor = sessionViewModel.appColors.primaryColor
    val primaryAccent = sessionViewModel.appColors.primaryAccent
    val secondaryColor = sessionViewModel.appColors.secondaryColor
    val secondaryAccent = sessionViewModel.appColors.secondaryAccent
    val userId = sessionViewModel.userId ?: -1
    val apiKey = sessionViewModel.getApiKey().orEmpty()
    val moreViewModel = hiltViewModel<MoreViewModel>()
    val showAppPrefs = moreViewModel.showAppPrefs
    val showProfilePrefs = moreViewModel.showProfilePrefs

    SideEffect {
        val activity = view.context as Activity
        activity.window.statusBarColor = primaryColor.toArgb()
        WindowCompat.getInsetsController(activity.window, view).isAppearanceLightStatusBars = true
    }

    LaunchedEffect(Unit) {
        moreViewModel.loadUserPreferences(
            apiKey = apiKey,
            userId = userId,
            updateStoredUserPrefs = { loadedPrefs ->
                sessionViewModel.updateUserPrefs(loadedPrefs)
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(primaryAccent)
            .statusBarsPadding()
            .imePadding()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(primaryColor)
                .align(Alignment.CenterHorizontally)
                .padding(6.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Settings",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.displaySmall.copy(
                        fontFamily = Ubuntu,
                        fontWeight = FontWeight.Bold
                    ),
                )

                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = "Settings gear",
                )
            }
        }

        Divider(
            color = secondaryColor,
            modifier = Modifier
                .height(1.dp)
                .fillMaxWidth()
        )

        Box(
            modifier = Modifier.weight(1f).padding(4.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = {
                        moreViewModel.onOpenAppPrefs()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = secondaryAccent),
                    elevation = ButtonDefaults.buttonElevation(7.dp),
                    border = BorderStroke(1.dp, secondaryColor),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                        )

                        Text(
                            text = "App Preferences",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontFamily = Ubuntu,
                                fontWeight = FontWeight.Bold
                            ),
                            color = secondaryColor
                        )

                        Icon(
                            imageVector = Icons.Filled.AppSettingsAlt,
                            contentDescription = "App Preferences",
                            tint = secondaryColor
                        )
                    }
                }

                Button(
                    onClick = {
                        moreViewModel.onOpenProfilePrefs()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = secondaryAccent),
                    border = BorderStroke(1.dp, secondaryColor),
                    elevation = ButtonDefaults.buttonElevation(7.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                        )

                        Text(
                            text = "Profile Preferences",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontFamily = Ubuntu,
                                fontWeight = FontWeight.Bold
                            ),
                            color = secondaryColor
                        )

                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = "Profile Preferences",
                            tint = secondaryColor
                        )
                    }
                }

                Box(
                    modifier = Modifier.height(40.dp)
                )

                Button(
                    onClick = {
                        sessionViewModel.handleLogout()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = secondaryColor),
                    elevation = ButtonDefaults.buttonElevation(7.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                        )

                        Text(
                            text = "Logout",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontFamily = Ubuntu,
                                fontWeight = FontWeight.Bold
                            ),
                            color = secondaryAccent
                        )

                        Icon(
                            imageVector = Icons.Filled.Logout,
                            contentDescription = "Logout",
                            tint = secondaryAccent
                        )
                    }
                }
            }
        }

        BottomNavBar()
    }

    if (showAppPrefs) {
        AppPreferencesDialog(
            onClose = {
                moreViewModel.onCloseAppPrefs()
            }
        )
    }

    if (showProfilePrefs) {
        ProfilePreferencesDialog(
            onClose = {
                moreViewModel.onCloseProfilePrefs()
            }
        )
    }

    if (moreViewModel.showUpdatePrefsSuccessToast) {
        Toast.makeText(LocalContext.current, "Successfully updated your preferences!", Toast.LENGTH_SHORT).show()
        moreViewModel.endShowUpdatePrefsSuccessToast()
    }
}
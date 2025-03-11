package io.winapps.voizy

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import io.winapps.voizy.ui.features.auth.login.LoginScreen
import io.winapps.voizy.ui.features.auth.splash.SplashScreen
import io.winapps.voizy.ui.features.profile.ProfileScreen
import io.winapps.voizy.ui.home.HomeScreen

@Composable
fun VoizyApp(
    sessionViewModel: SessionViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val gradientColors = listOf(
        Color(0xFFF9D841),
        Color(0xFFF9D841)
    )
    val didFinishSplash = sessionViewModel.didFinishSplash
    val isLoggedIn = sessionViewModel.isLoggedIn

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(gradientColors))
    ) {
        when {
            !didFinishSplash -> {
                SplashScreen()
            }
            !isLoggedIn -> {
                LoginScreen(
                    onLoginSuccess = { sessionViewModel.markLoggedIn() }
                )
            }
            else -> {
//                HomeScreen()
                ProfileScreen()
            }
        }
    }
}
package io.winapps.voizy.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen() {
    val configuration = LocalConfiguration.current
    val horizontalPadding = if (configuration.screenWidthDp >= 600) 200.dp else 10.dp

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFFF9D841),
                        Color(0xFFF9D841)
                    )
                )
            )
            .systemBarsPadding()
            .imePadding()
            .padding(horizontal = horizontalPadding, vertical = 10.dp)
    ) {
        Text("Home page here!")
    }
}
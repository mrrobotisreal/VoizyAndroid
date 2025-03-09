package io.winapps.voizy.ui.features.auth.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import io.winapps.voizy.R

@Composable
fun SplashScreen() {
    val configuration = LocalConfiguration.current
    val horizontalPadding = if (configuration.screenWidthDp >= 600) 200.dp else 10.dp

    Box(modifier = Modifier.fillMaxSize().padding(horizontal = horizontalPadding)) {
        Image(
            painter = painterResource(id = R.drawable.voizy_logo_yellow),
            contentDescription = "Main splash image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(240.dp).clip(CircleShape).shadow(
                elevation = 7.dp,
                shape = CircleShape
            )
                .align(Alignment.Center)
        )
    }
}
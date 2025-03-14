package io.winapps.voizy.ui.features.auth.login

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.winapps.voizy.ui.theme.Ubuntu

@Composable
fun HelpDialog(
    onClose: () -> Unit
) {
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
                .padding(24.dp)
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
                    text = "Need Help?",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.Black,
                    fontFamily = Ubuntu,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "If you're having trouble logging in then you can navigate to https://voizy.me/help to reset your password or see what other options are available to you.\n\n" +
                    "If you're having trouble creating a new account, make sure that you've correctly spelled your email address and that your username and password conform to the requirements, " +
                    "but if you're still having issues you can also reach out to the link above.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.DarkGray,
                    fontFamily = Ubuntu,
                    fontWeight = FontWeight.Normal
                )

                Button(
                    onClick = onClose,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF10E91))
                ) {
                    Text("Close", color = Color(0xFFFFD5ED), fontFamily = Ubuntu, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

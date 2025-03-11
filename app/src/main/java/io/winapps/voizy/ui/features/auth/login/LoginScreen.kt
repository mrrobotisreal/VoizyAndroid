package io.winapps.voizy.ui.features.auth.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.winapps.voizy.R
import io.winapps.voizy.SessionViewModel
import io.winapps.voizy.ui.theme.Charm
import io.winapps.voizy.ui.theme.Ubuntu

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    loginViewModel: LoginViewModel = hiltViewModel(),
    sessionViewModel: SessionViewModel = hiltViewModel()
) {
    val configuration = LocalConfiguration.current
    val horizontalPadding = if (configuration.screenWidthDp >= 600) 200.dp else 10.dp
    val loginState = loginViewModel.loginState
    val createAccountState = loginViewModel.createAccountState
    var isHelpDialogOpen by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
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
            Image(
                painter = painterResource(id = R.drawable.voizy_logo_yellow),
                contentDescription = "Voizy logo",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color(0xFFF10E91), CircleShape)
                    .shadow(elevation = 7.dp, shape = CircleShape)
                    .align(Alignment.TopCenter)
            )

            Card(
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 7.dp), // maybe 5.dp, we'll see though
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth()
                    .border(2.dp, Color(0xFFF10E91), RoundedCornerShape(12.dp)),
                colors = cardColors(
                    containerColor = Color(0xFFFDF4C9),
                )
            ) {
                if (!loginViewModel.isRegistering) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Voizy",
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold,
                            fontFamily = Charm,
                            color = Color.Black
                        )

                        OutlinedTextField(
                            value = loginViewModel.username,
                            onValueChange = { loginViewModel.onUsernameChanged(it) },
                            label = { Text("Username", fontFamily = Ubuntu, fontWeight = FontWeight.Normal) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                focusedTextColor = Color.Black,
                                focusedLabelColor = Color.DarkGray,
                                unfocusedContainerColor = Color.White,
                                unfocusedTextColor = Color.Black,
                                unfocusedLabelColor = Color.DarkGray
                            )
                        )

                        OutlinedTextField(
                            value = loginViewModel.password,
                            onValueChange = { loginViewModel.onPasswordChanged(it) },
                            label = { Text("Password", fontFamily = Ubuntu, fontWeight = FontWeight.Normal) },
                            visualTransformation = PasswordVisualTransformation(),
                            modifier = Modifier.fillMaxWidth(),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                focusedTextColor = Color.Black,
                                focusedLabelColor = Color.DarkGray,
                                unfocusedContainerColor = Color.White,
                                unfocusedTextColor = Color.Black,
                                unfocusedLabelColor = Color.DarkGray
                            )
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextButton(
                                onClick = { loginViewModel.toggleRegistration() }
                            ) {
                                Text(
                                    text = "Need an account?",
                                    color = Color(0xFFF10E91),
                                    fontFamily = Ubuntu,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            if (loginState is LoginState.Loading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = Color(0xFFF10E91)
                                )
                            } else {
                                Button(
                                    onClick = { loginViewModel.onLogin() },
                                    colors = buttonColors(containerColor = Color(0xFFF10E91))
                                ) {
                                    Text("Login", fontFamily = Ubuntu, fontWeight = FontWeight.Bold)
                                }
                            }

                            when(loginState) {
                                is LoginState.Idle -> {
                                    // do nothing
                                }
                                is LoginState.Loading -> {
                                    // do nothing
                                }
                                is LoginState.Success -> {
                                    val response = loginState.response

                                    sessionViewModel.setUserData(
                                        userId = response.userID,
                                        username = response.username,
                                        apiKey = response.apiKey,
                                        token = response.token
                                    )

                                    onLoginSuccess()
                                }
                                is LoginState.Error -> {
                                    // TODO: add logic here for handling the error
                                }
                            }
                        }
                    }
                } else {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Voizy",
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold,
                            fontFamily = Charm,
                            color = Color.Black
                        )

                        OutlinedTextField(
                            value = loginViewModel.email,
                            onValueChange = { loginViewModel.onEmailChanged(it) },
                            label = { Text("Email", fontFamily = Ubuntu, fontWeight = FontWeight.Normal) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                focusedTextColor = Color.Black,
                                focusedLabelColor = Color.DarkGray,
                                unfocusedContainerColor = Color.White,
                                unfocusedTextColor = Color.Black,
                                unfocusedLabelColor = Color.DarkGray
                            )
                        )

                        OutlinedTextField(
                            value = loginViewModel.preferredName,
                            onValueChange = { loginViewModel.onPreferredNameChanged(it) },
                            label = { Text("Preferred name", fontFamily = Ubuntu, fontWeight = FontWeight.Normal) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                focusedTextColor = Color.Black,
                                focusedLabelColor = Color.DarkGray,
                                unfocusedContainerColor = Color.White,
                                unfocusedTextColor = Color.Black,
                                unfocusedLabelColor = Color.DarkGray
                            )
                        )

                        OutlinedTextField(
                            value = loginViewModel.username,
                            onValueChange = { loginViewModel.onUsernameChanged(it) },
                            label = { Text("Username", fontFamily = Ubuntu, fontWeight = FontWeight.Normal) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                focusedTextColor = Color.Black,
                                focusedLabelColor = Color.DarkGray,
                                unfocusedContainerColor = Color.White,
                                unfocusedTextColor = Color.Black,
                                unfocusedLabelColor = Color.DarkGray
                            )
                        )

                        OutlinedTextField(
                            value = loginViewModel.password,
                            onValueChange = { loginViewModel.onPasswordChanged(it) },
                            label = { Text("Password", fontFamily = Ubuntu, fontWeight = FontWeight.Normal) },
                            visualTransformation = PasswordVisualTransformation(),
                            modifier = Modifier.fillMaxWidth(),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                focusedTextColor = Color.Black,
                                focusedLabelColor = Color.DarkGray,
                                unfocusedContainerColor = Color.White,
                                unfocusedTextColor = Color.Black,
                                unfocusedLabelColor = Color.DarkGray
                            )
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextButton(onClick = { loginViewModel.toggleRegistration() }) {
                                Text(
                                    text = "Have an account?",
                                    color = Color(0xFFF10E91),
                                    fontFamily = Ubuntu,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            if (createAccountState is CreateAccountState.Loading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = Color(0xFFF10E91)
                                )
                            } else {
                                Button(
                                    onClick = { loginViewModel.onCreateAccount() },
                                    colors = buttonColors(containerColor = Color(0xFFF10E91))
                                ) {
                                    Text("Create", fontFamily = Ubuntu, fontWeight = FontWeight.Bold)
                                }
                            }

                            when(createAccountState) {
                                is CreateAccountState.Idle -> {
                                    // do nothing
                                }
                                is CreateAccountState.Loading -> {
                                    // do nothing
                                }
                                is CreateAccountState.Success -> {
                                    onLoginSuccess()
                                }
                                is CreateAccountState.Error -> {
                                    // TODO: add logic here for handling the error
                                }
                            }
                        }
                    }
                }
            }

            FloatingActionButton(
                onClick = {
                    isHelpDialogOpen = true
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .clip(CircleShape)
                    .padding(16.dp)
                    .size(48.dp),
                containerColor = Color(0xFFF10E91),
                contentColor = Color(0xFFFFD5ED)
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Help info",
                )
            }
        }

        if (isHelpDialogOpen) {
            HelpDialog(
                onClose = { isHelpDialogOpen = false }
            )
        }
    }
}
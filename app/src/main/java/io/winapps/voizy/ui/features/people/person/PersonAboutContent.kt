package io.winapps.voizy.ui.features.people.person

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.winapps.voizy.SessionViewModel
import io.winapps.voizy.ui.theme.Ubuntu
import io.winapps.voizy.util.getFormattedDateString

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PersonAboutContent() {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
            .padding(8.dp)
    ) {
        PersonProfileInfoCard()
        Spacer(modifier = Modifier.height(10.dp))
        PersonInterestsCard()
        Spacer(modifier = Modifier.height(10.dp))
        PersonEducationCard()
        Spacer(modifier = Modifier.height(10.dp))
        PersonSocialLinksTable()
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PersonProfileInfoCard() {
    val sessionViewModel = hiltViewModel<SessionViewModel>()
    val profileViewModel = hiltViewModel<PersonViewModel>()
    val isLoading = profileViewModel.isLoading
    val firstName = profileViewModel.firstName
    val lastName = profileViewModel.lastName
    val preferredName = profileViewModel.preferredName
    val birthDate = profileViewModel.birthDate
    val cityOfResidence = profileViewModel.cityOfResidence
    val placeOfWork = profileViewModel.placeOfWork
    val dateJoined = profileViewModel.dateJoined

    LaunchedEffect(Unit) {
        val userId = sessionViewModel.userId ?: -1
        val apiKey = sessionViewModel.getApiKey().orEmpty()
        profileViewModel.loadProfileInfo(
            personId = profileViewModel.selectedPersonId,
            userId = userId,
            apiKey = apiKey
        )
    }

    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Color(0xFFF10E91)
                )
            }
        } else {
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    text = "Profile Info",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontFamily = Ubuntu,
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color.Black,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Row {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "First name:",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontFamily = Ubuntu,
                                    fontWeight = FontWeight.Bold
                                ),
                                color = Color.Black
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = firstName ?: "(empty)",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontFamily = Ubuntu,
                                    fontWeight = FontWeight.Normal
                                ),
                                color = Color.DarkGray
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
                Row {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Last name:",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontFamily = Ubuntu,
                                    fontWeight = FontWeight.Bold
                                ),
                                color = Color.Black
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = lastName ?: "(empty)",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontFamily = Ubuntu,
                                    fontWeight = FontWeight.Normal
                                ),
                                color = Color.DarkGray
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
                Row {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Preferred name:",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontFamily = Ubuntu,
                                    fontWeight = FontWeight.Bold
                                ),
                                color = Color.Black
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = preferredName,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontFamily = Ubuntu,
                                    fontWeight = FontWeight.Normal
                                ),
                                color = Color.DarkGray
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
                Row {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Birth date:",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontFamily = Ubuntu,
                                    fontWeight = FontWeight.Bold
                                ),
                                color = Color.Black
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = if (birthDate != null) getFormattedDateString(birthDate) else "(empty)",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontFamily = Ubuntu,
                                    fontWeight = FontWeight.Normal
                                ),
                                color = Color.DarkGray
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
                Row {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "City of residence:",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontFamily = Ubuntu,
                                    fontWeight = FontWeight.Bold
                                ),
                                color = Color.Black
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = cityOfResidence ?: "(empty)",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontFamily = Ubuntu,
                                    fontWeight = FontWeight.Normal
                                ),
                                color = Color.DarkGray
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
                Row {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Place of work:",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontFamily = Ubuntu,
                                    fontWeight = FontWeight.Bold
                                ),
                                color = Color.Black
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = placeOfWork ?: "(empty)",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontFamily = Ubuntu,
                                    fontWeight = FontWeight.Normal
                                ),
                                color = Color.DarkGray
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
                Row {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Date joined:",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontFamily = Ubuntu,
                                    fontWeight = FontWeight.Bold
                                ),
                                color = Color.Black
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = if (dateJoined != null) getFormattedDateString(dateJoined) else "(empty)",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontFamily = Ubuntu,
                                    fontWeight = FontWeight.Normal
                                ),
                                color = Color.DarkGray
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PersonInterestsCard() {
    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = "Interests",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontFamily = Ubuntu,
                    fontWeight = FontWeight.Bold
                ),
                color = Color.Black,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Row {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "No interests have been added yet...",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontFamily = Ubuntu,
                                fontWeight = FontWeight.Normal
                            ),
                            color = Color.DarkGray
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PersonEducationCard() {
    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = "Education",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontFamily = Ubuntu,
                    fontWeight = FontWeight.Bold
                ),
                color = Color.Black,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Row {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "No education has been added yet...",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontFamily = Ubuntu,
                                fontWeight = FontWeight.Normal
                            ),
                            color = Color.DarkGray
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PersonSocialLinksTable() {
    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = "Social Links",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontFamily = Ubuntu,
                    fontWeight = FontWeight.Bold
                ),
                color = Color.Black,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Row {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "No social links have been added yet...",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontFamily = Ubuntu,
                                fontWeight = FontWeight.Normal
                            ),
                            color = Color.DarkGray
                        )
                    }
                }
            }
        }
    }
}
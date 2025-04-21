package io.winapps.voizy.ui.features.people

import android.app.Activity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonSearch
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import io.winapps.voizy.AppScreen
import io.winapps.voizy.SessionViewModel
import io.winapps.voizy.data.model.users.CompletePerson
import io.winapps.voizy.ui.features.people.person.PersonViewModel
import io.winapps.voizy.ui.navigation.BottomNavBar
import io.winapps.voizy.ui.theme.Ubuntu

@Composable
fun PeopleScreen() {
    val view = LocalView.current
    val sessionViewModel = hiltViewModel<SessionViewModel>()
    val peopleViewModel = hiltViewModel<PeopleViewModel>()
    val isLoading = peopleViewModel.isLoading
    val errorMessage = peopleViewModel.errorMessage
    val peopleYouMayKnow = peopleViewModel.peopleYouMayKnow
    val searchText = peopleViewModel.searchText
    val searchResults = peopleViewModel.searchResults

    SideEffect {
        val activity = view.context as Activity
        activity.window.statusBarColor = Color(0xFFF9D841).toArgb()
        WindowCompat.getInsetsController(activity.window, view).isAppearanceLightStatusBars = true
    }

    LaunchedEffect(Unit) {
        val userId = sessionViewModel.userId ?: 0
        val apiKey = sessionViewModel.getApiKey().orEmpty()
        peopleViewModel.loadPeopleYouMayKnow(
            userId = userId,
            apiKey = apiKey,
            limit = 30,
            page = 1,
            forceRefresh = false
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFDF4C9))
            .statusBarsPadding()
            .imePadding()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF9D841))
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.padding(4.dp).fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = searchText,
                    onValueChange = {
                        peopleViewModel.onSearchTextChanged(it)
                    },
                    label = {
                        Text(
                            "Search People",
                            fontFamily = Ubuntu,
                            fontWeight = FontWeight.Normal
                        )
                    },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Search",
                            tint = Color(0xFFF10E91)
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        focusedTextColor = Color.Black,
                        focusedLabelColor = Color.DarkGray,
                        unfocusedContainerColor = Color.White,
                        unfocusedTextColor = Color.Black,
                        unfocusedLabelColor = Color.DarkGray
                    )
                )

                IconButton(
                    onClick = { peopleViewModel.onSearchPeople(searchText) },
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = Color(0xFFF10E91)
                    )
                ) {
                    Icon(
                        imageVector = Icons.Filled.PersonSearch,
                        contentDescription = "Search People",
                        tint = Color(0xFFFFD5ED)
                    )
                }
            }
        }

        Divider(
            color = Color(0xFFF10E91),
            modifier = Modifier
                .height(1.dp)
                .fillMaxWidth()
        )

        Box(
            modifier = Modifier.weight(1f)
        ) {
            PeopleContent(
                people = peopleYouMayKnow,
                isLoading = isLoading,
                errorMessage = errorMessage,
            )
        }

        BottomNavBar()
    }
}

@Composable
fun PeopleContent(
    people: List<CompletePerson>,
    isLoading: Boolean,
    errorMessage: String?
) {
    val sessionViewModel = hiltViewModel<SessionViewModel>()
    val personViewModel = hiltViewModel<PersonViewModel>()

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
        ) {
            Text(
                text = "People You May Know",
                textAlign = TextAlign.Center,
                fontFamily = Ubuntu,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )
        }

        Box(
            modifier = Modifier.fillMaxSize().padding(2.dp)
        ) {
            when {
                isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = Color(0xFFF10E91)
                    )
                }
                errorMessage != null -> {
                    Text(
                        text = "Error: $errorMessage",
                        modifier = Modifier.align(Alignment.Center),
                        fontFamily = Ubuntu,
                        fontWeight = FontWeight.Normal
                    )
                }
                else -> {
                    LazyColumn(
                        contentPadding = PaddingValues(2.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        items(people) { person ->
                            Box(
                                modifier = Modifier.padding(2.dp)
                            ) {
                                PersonCard(
                                    person = person,
                                    onClick = { personToView ->
                                        personViewModel.selectPerson(personToView.userID, personToView.username)
                                        sessionViewModel.switchCurrentAppScreen(AppScreen.PERSON)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PersonCard(
    person: CompletePerson,
    onClick: (CompletePerson) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .border(1.dp, Color(0xFFF10E91), RoundedCornerShape(12.dp))
            .clickable { onClick(person) },
        elevation = CardDefaults.cardElevation(7.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.background(Color.White).padding(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFFD5ED))
                        .border(2.dp, Color(0xFFF10E91), CircleShape)
                ) {
                    if (!person.profilePicURL.isNullOrEmpty()) {
                        val painter = rememberAsyncImagePainter(
                            model = person.profilePicURL
                        )
                        Image(
                            painter = painter,
                            contentDescription = "Person image",
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                                .border(2.dp, Color(0xFFF10E91), CircleShape),
                            contentScale = ContentScale.Crop
                        )

                        if (painter.state is AsyncImagePainter.State.Loading) {
                            CircularProgressIndicator(
                                modifier = Modifier.align(Alignment.Center),
                                color = Color(0xFFF10E91)
                            )
                        }
                    } else {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Empty person profile pic",
                            modifier = Modifier
                                .clip(CircleShape)
                                .align(Alignment.Center),
                            tint = Color(0xFFF10E91)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = person.displayName,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontFamily = Ubuntu,
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color.Black
                    )

                    if (!person.cityOfResidence.isNullOrBlank()) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = Color(0xFFF10E91),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = person.cityOfResidence,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontFamily = Ubuntu,
                                    fontWeight = FontWeight.Normal
                                ),
                                color = Color(0xFFF10E91)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            val friendsInCommonText = if (person.friendsInCommon.isEmpty()) {
                "No friends in common"
            } else if (person.friendsInCommon.size == 1) {
                "1 friend in common"
            } else {
                "${person.friendsInCommon.size} friends in common"
            }
            Text(
                text = friendsInCommonText,
                textAlign = TextAlign.Center,
                fontFamily = Ubuntu,
                fontWeight = FontWeight.Normal,
                style = MaterialTheme.typography.bodySmall,
                color = Color.DarkGray
            )

            LazyRow(
                modifier = modifier
                    .padding(2.dp),
                contentPadding = PaddingValues(2.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                items(person.friendsInCommon) { friend ->
                    Box(
                        modifier = modifier.padding(horizontal = 2.dp)
                    ) {
                        TextButton(
                            onClick = {},
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD5ED)),
                            border = BorderStroke(1.dp, Color(0xFFF10E91))
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.SpaceAround,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFFFD5ED))
                                        .border(1.dp, Color(0xFFF10E91), CircleShape)
                                ) {
                                    if (!friend.profilePicURL.isNullOrEmpty()) {
                                        val friendPainter = rememberAsyncImagePainter(
                                            model = friend.profilePicURL
                                        )
                                        Image(
                                            painter = friendPainter,
                                            contentDescription = "Friend in common image",
                                            modifier = Modifier
                                                .size(40.dp)
                                                .clip(CircleShape)
                                                .border(1.dp, Color(0xFFF10E91), CircleShape),
                                            contentScale = ContentScale.Crop
                                        )

                                        if (friendPainter.state is AsyncImagePainter.State.Loading) {
                                            CircularProgressIndicator(
                                                modifier = Modifier.align(Alignment.Center),
                                                color = Color(0xFFF10E91)
                                            )
                                        }
                                    } else {
                                        Icon(
                                            imageVector = Icons.Default.Person,
                                            contentDescription = "Empty friend in common profile pic",
                                            modifier = Modifier
                                                .clip(CircleShape)
                                                .align(Alignment.Center),
                                            tint = Color(0xFFF10E91)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(4.dp))

                                Text(
                                    text = friend.displayName,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontFamily = Ubuntu,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = Color(0xFFF10E91)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
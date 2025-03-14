package io.winapps.voizy.ui.features.profile

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import io.winapps.voizy.ui.theme.Ubuntu
import kotlinx.coroutines.launch

//@OptIn(ExperimentalFoundationApi::class)
//@Composable
//fun LocationPicker(
//    initialLat: Double?,
//    initialLong: Double?,
//    initialLocationName: String,
//    onBack: () -> Unit,
//    onAdd: (displayName: String, lat: Double, long: Double) -> Unit
//) {
//    var searchText by remember { mutableStateOf("") }
//    var locationName by remember { mutableStateOf(initialLocationName) }
//    var lat by remember { mutableStateOf(initialLat ?: 0.0) }
//    var long by remember { mutableStateOf(initialLong ?: 0.0) }
//
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(8.dp),
//        verticalArrangement = Arrangement.spacedBy(8.dp)
//    ) {
//        OutlinedTextField(
//            value = searchText,
//            onValueChange = { newText ->
//                searchText = newText
//                // call my geocoding / place API here to update the map, etc.
//            },
//            label = { Text("Search location") },
//            trailingIcon = {
//                Icon(Icons.Filled.Search, contentDescription = "Search")
//            },
//            modifier = Modifier.fillMaxWidth()
//        )
//
//        Box(
//            modifier = Modifier
//                .weight(1f)
//                .fillMaxWidth()
//                .background(Color.LightGray)
//                .clickable {
//                    // user taps => set lat/long from the tapped coordinate
//                    lat = 37.4219999
//                    long = -122.0840575
//                    locationName = "Some place" // optional auto fill
//                }
//        ) {
//            Text(
//                "Map Here (Tap to pick location)",
//                modifier = Modifier.align(Alignment.Center)
//            )
//        }
//
//        OutlinedTextField(
//            value = locationName,
//            onValueChange = { locationName = it },
//            label = { Text("Location name") },
//            modifier = Modifier.fillMaxWidth()
//        )
//
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceBetween
//        ) {
//            TextButton(onClick = onBack) {
//                Text(
//                    "Back",
//                    color = Color(0xFFF10E91)
//                )
//            }
//            Button(onClick = {
//                // user tapped add => pass name, lat, long
//                onAdd(locationName, lat, long)
//            },
//                colors = buttonColors(containerColor = Color(0xFFF10E91))
//            ) {
//                Text("Add", color = Color.White)
//            }
//        }
//    }
//}

@Composable
fun LocationPickerUI(
    locationPickerViewModel: LocationPickerViewModel = hiltViewModel(),
    onBack: () -> Unit,
    onAdd: (displayName: String, lat: Double, long: Double) -> Unit
) {
    val scope = rememberCoroutineScope()

    val userLat = locationPickerViewModel.userLat
    val userLng = locationPickerViewModel.userLng

    val chosenLat = locationPickerViewModel.chosenLat
    val chosenLng = locationPickerViewModel.chosenLng
    val chosenName = locationPickerViewModel.chosenName

    val cameraPositionState = rememberCameraPositionState()

    LaunchedEffect(userLat, userLng) {
        cameraPositionState.position = CameraPosition.fromLatLngZoom(
            LatLng(userLat, userLng),
            14f
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box {
            OutlinedTextField(
                value = locationPickerViewModel.searchQuery,
                onValueChange = {
                    locationPickerViewModel.onSearchQueryChanged(it)
                },
                label = { Text("Search location") },
                trailingIcon = {
                    IconButton(onClick = {
                        //
                    }) {
                        Icon(Icons.Filled.Search, contentDescription = "Search")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    focusedTextColor = Color.Black,
                    focusedLabelColor = Color.DarkGray,
                    unfocusedContainerColor = Color.White,
                    unfocusedTextColor = Color.Black,
                    unfocusedLabelColor = Color.DarkGray
                )
            )

            if (locationPickerViewModel.isDropDownExpanded && locationPickerViewModel.suggestions.isNotEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 56.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column {
                        locationPickerViewModel.suggestions.forEach { prediction ->
                            val mainText = prediction.getPrimaryText(null).toString()
                            val secondaryText = prediction.getSecondaryText(null).toString()
                            val display = "$mainText, $secondaryText"

                            Text(
                                text = display,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        scope.launch {
                                            locationPickerViewModel.selectSuggestion(prediction, cameraPositionState)
//                                            val lat = locationPickerViewModel.chosenLat
//                                            val lng = locationPickerViewModel.chosenLng
//                                            if (lat != null && lng != null) {
//                                                cameraPositionState.position = CameraPosition.fromLatLngZoom(
//                                                    LatLng(lat, lng),
//                                                    14f
//                                                )
//                                            }
                                        }
                                    }
                                    .padding(8.dp)
                            )
                        }
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                onMapClick = { latLng: LatLng ->
                    locationPickerViewModel.onMapClick(
                        latLng.latitude, latLng.longitude
                    )
                }
            ) {
                chosenLat?.let { lat ->
                    chosenLng?.let { lng ->
                        Marker(
                            state = MarkerState(position = LatLng(lat, lng)),
                            title = chosenName
                        )
                    }
                }
            }
        }

        OutlinedTextField(
            value = chosenName,
            onValueChange = { locationPickerViewModel.chosenName = it },
            label = { Text("Location name") },
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
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(onClick = { onBack() }) {
                Text(
                    text = "Back",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontFamily = Ubuntu,
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color(0xFFF10E91)
                )
            }

            Button(
                onClick = {
                    val finalLat = chosenLat ?: userLat
                    val finalLng = chosenLng ?: userLng
                    onAdd(chosenName, finalLat, finalLng)
                },
                colors = buttonColors(containerColor = Color(0xFFF10E91))
            ) {
                Text(
                    text = "Add",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontFamily = Ubuntu,
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color(0xFFFFD5ED)
                )
            }
        }
    }
}
package io.winapps.voizy.ui.features.profile

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.maps.android.compose.CameraPositionState
import dagger.hilt.android.lifecycle.HiltViewModel
import io.winapps.voizy.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import javax.inject.Inject

data class PlaceSuggestion(
    val displayName: String,
    val lat: Double,
    val lng: Double
)

@HiltViewModel
class LocationPickerViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {
    private val placesClient: PlacesClient = Places.createClient(application.applicationContext)

    var userLat by mutableDoubleStateOf(37.4219999)
    var userLng by mutableDoubleStateOf(-122.0840575)

    var chosenLat by mutableStateOf<Double?>(null)
    var chosenLng by mutableStateOf<Double?>(null)
    var chosenName by mutableStateOf("")

    var searchQuery by mutableStateOf("")
    var suggestions by mutableStateOf<List<AutocompletePrediction>>(emptyList())
    var isDropDownExpanded by mutableStateOf(false)

    init {
        // We'll try to get the device location
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(application)
        if (ContextCompat.checkSelfPermission(application, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    if (location != null) {
                        userLat = location.latitude
                        userLng = location.longitude
                        chosenLat = location.latitude
                        chosenLng = location.longitude
                    }
                }
        }
    }

    fun onSearchQueryChanged(newValue: String) {
        searchQuery = newValue
        if (newValue.length < 2) {
            suggestions = emptyList()
            isDropDownExpanded = false
            return
        }

        val request = FindAutocompletePredictionsRequest.builder()
            .setQuery(newValue)
            .build()

        placesClient.findAutocompletePredictions(request)
            .addOnSuccessListener { response ->
                Log.d("PlacesAutocomplete", "Got predictions: ${response.autocompletePredictions.size}")
                suggestions = response.autocompletePredictions
                isDropDownExpanded = suggestions.isNotEmpty()
            }
            .addOnFailureListener { e ->
                Log.e("Places autocomplete", "Autocomplete request failed", e)
                suggestions = emptyList()
                isDropDownExpanded = false
            }
    }

    suspend fun selectSuggestion(prediction: AutocompletePrediction, cameraPositionState: CameraPositionState) {
        val placeId = prediction.placeId
        val placeFields = listOf(Place.Field.LAT_LNG, Place.Field.NAME, Place.Field.ADDRESS)

        val placeRequest = FetchPlaceRequest.builder(placeId, placeFields).build()
        placesClient.fetchPlace(placeRequest)
            .addOnSuccessListener { placeResponse ->
                val place = placeResponse.place
                val latLng = place.latLng
                if (latLng != null) {
                    chosenLat = latLng.latitude
                    chosenLng = latLng.longitude
                    chosenName = place.displayName ?: place.formattedAddress ?: ""
                    cameraPositionState.position = CameraPosition.fromLatLngZoom(
                        latLng, 14f
                    )
                }
                isDropDownExpanded = false
            }
            .addOnFailureListener { e ->
                Log.e("Places select", "Select request failed", e)
                // TODO: handle this later
            }
    }

    fun onMapClick(lat: Double, lng: Double) {
        chosenLat = lat
        chosenLng = lng

        viewModelScope.launch {
            val address = reverseGeocode(lat, lng, BuildConfig.MAPS_API_KEY)
            chosenName = address ?: "Lat=$lat, Lng=$lng"
        }
    }

    private suspend fun reverseGeocode(lat: Double, lng: Double, apiKey: String): String? {
        val url = "https://maps.googleapis.com/maps/api/geocode/json?latlng=$lat,$lng&key=$apiKey"
        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()

        return withContext(Dispatchers.IO) {
            val response = client.newCall(request).execute()
            if (!response.isSuccessful) return@withContext null
            val body = response.body?.string() ?: return@withContext null

            val json = JSONObject(body)
            val results = json.optJSONArray("results")
            if (results != null && results.length() > 0) {
                val first = results.getJSONObject(0)
                first.optString("formatted_address")
            } else null
        }
    }
}
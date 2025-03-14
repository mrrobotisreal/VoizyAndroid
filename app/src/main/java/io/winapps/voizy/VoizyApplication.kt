package io.winapps.voizy

import android.app.Application
import android.util.Log
import com.google.android.libraries.places.api.Places
import dagger.hilt.android.HiltAndroidApp
import io.winapps.voizy.BuildConfig

@HiltAndroidApp
class VoizyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (!Places.isInitialized()) {
            Log.d("BuildConfig:", BuildConfig.MAPS_API_KEY)
            Places.initialize(this, BuildConfig.MAPS_API_KEY)
        }
    }
}
package io.winapps.voizy

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class VoizyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}
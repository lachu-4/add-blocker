package com.zeroads

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Base Application class for ZeroAds.
 * Initializes Hilt for Dependency Injection.
 */
@HiltAndroidApp
class ZeroAdsApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize global settings, analytics, or background tasks here
    }
}

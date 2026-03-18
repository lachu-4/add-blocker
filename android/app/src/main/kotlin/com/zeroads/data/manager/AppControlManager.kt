package com.zeroads.data.manager

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.util.Log

/**
 * App-Level Control Module.
 * Manages whitelisting and per-app ad-blocking rules.
 */
class AppControlManager(private val context: Context) {

    private val packageManager: PackageManager = context.packageManager

    /**
     * Retrieves a list of all installed applications.
     */
    fun getInstalledApps(includeSystemApps: Boolean = true): List<AppInfo> {
        val apps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
        return apps.filter { app ->
            if (includeSystemApps) true else (app.flags and ApplicationInfo.FLAG_SYSTEM) == 0
        }.map { app ->
            AppInfo(
                packageName = app.packageName,
                appName = packageManager.getApplicationLabel(app).toString(),
                icon = packageManager.getApplicationIcon(app),
                isSystemApp = (app.flags and ApplicationInfo.FLAG_SYSTEM) != 0,
                isWhitelisted = isAppWhitelisted(app.packageName)
            )
        }.sortedBy { it.appName.lowercase() }
    }

    /**
     * Checks if an app is whitelisted from ad-blocking.
     */
    fun isAppWhitelisted(packageName: String): Boolean {
        // In production, we'd query a Room DB or SharedPreferences
        val prefs = context.getSharedPreferences("ZeroAdsPrefs", Context.MODE_PRIVATE)
        return prefs.getBoolean("whitelist_$packageName", false)
    }

    /**
     * Toggles the whitelist status of an app.
     */
    fun toggleWhitelist(packageName: String, isWhitelisted: Boolean) {
        val prefs = context.getSharedPreferences("ZeroAdsPrefs", Context.MODE_PRIVATE)
        prefs.edit().putBoolean("whitelist_$packageName", isWhitelisted).apply()
        Log.d("AppControlManager", "App $packageName whitelisted: $isWhitelisted")
    }
}

data class AppInfo(
    val packageName: String,
    val appName: String,
    val icon: android.graphics.drawable.Drawable,
    val isSystemApp: Boolean,
    var isWhitelisted: Boolean
)

package com.zeroads.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Dashboard : Screen("dashboard", "Dashboard", Icons.Default.Dashboard)
    object AppControl : Screen("app_control", "App Control", Icons.Default.Apps)
    object DnsSettings : Screen("dns_settings", "DNS Settings", Icons.Default.Dns)
    object Stats : Screen("stats", "Stats", Icons.Default.BarChart)
}

val navItems = listOf(
    Screen.Dashboard,
    Screen.AppControl,
    Screen.DnsSettings,
    Screen.Stats
)

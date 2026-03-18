package com.zeroads.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zeroads.data.db.TrafficLog
import com.zeroads.data.db.ZeroAdsDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

import com.zeroads.data.manager.AppControlManager
import com.zeroads.data.manager.AppInfo

import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

/**
 * Dashboard ViewModel for ZeroAds.
 * Manages real-time stats, traffic logs, and app selection for the UI.
 */
@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val dao: ZeroAdsDao,
    private val appControlManager: AppControlManager
) : ViewModel() {

    private val _stats = MutableStateFlow(DashboardStats())
    val stats: StateFlow<DashboardStats> = _stats.asStateFlow()

    private val _trafficLogs = MutableStateFlow<List<TrafficLog>>(emptyList())
    val trafficLogs: StateFlow<List<TrafficLog>> = _trafficLogs.asStateFlow()

    private val _installedApps = MutableStateFlow<List<AppInfo>>(emptyList())
    private val _searchQuery = MutableStateFlow("")
    
    val installedApps: StateFlow<List<AppInfo>> = combine(_installedApps, _searchQuery) { apps, query ->
        if (query.isBlank()) apps
        else apps.filter { it.appName.contains(query, ignoreCase = true) || it.packageName.contains(query, ignoreCase = true) }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    private val _isAppSelectionVisible = MutableStateFlow(false)
    val isAppSelectionVisible: StateFlow<Boolean> = _isAppSelectionVisible.asStateFlow()

    private val _isAppsLoading = MutableStateFlow(false)
    val isAppsLoading: StateFlow<Boolean> = _isAppsLoading.asStateFlow()

    private val _selectedDns = MutableStateFlow("AdGuard DNS")
    val selectedDns: StateFlow<String> = _selectedDns.asStateFlow()

    private val _showSystemApps = MutableStateFlow(false)
    val showSystemApps: StateFlow<Boolean> = _showSystemApps.asStateFlow()

    private val _isProtected = MutableStateFlow(false)
    val isProtected: StateFlow<Boolean> = _isProtected.asStateFlow()

    init {
        refreshStats()
        refreshTrafficLogs()
        loadInstalledApps()
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun selectAll(select: Boolean) {
        viewModelScope.launch {
            _installedApps.value.forEach { app ->
                appControlManager.toggleWhitelist(app.packageName, !select)
            }
            loadInstalledApps()
        }
    }

    fun loadInstalledApps() {
        viewModelScope.launch {
            _isAppsLoading.value = true
            try {
                val apps = appControlManager.getInstalledApps(includeSystemApps = _showSystemApps.value)
                Log.d("DashboardViewModel", "Loaded ${apps.size} apps")
                _installedApps.value = apps
            } catch (e: Exception) {
                Log.e("DashboardViewModel", "Error loading apps", e)
            } finally {
                _isAppsLoading.value = false
            }
        }
    }

    fun toggleSystemApps(show: Boolean) {
        _showSystemApps.value = show
        loadInstalledApps()
    }

    fun hasProtectedApps(): Boolean {
        return _installedApps.value.any { !it.isWhitelisted }
    }

    fun toggleAppSelection(packageName: String, isSelected: Boolean) {
        viewModelScope.launch {
            appControlManager.toggleWhitelist(packageName, !isSelected) // Whitelist = NOT selected for blocking
            loadInstalledApps()
        }
    }

    fun setProtection(active: Boolean) {
        _isProtected.value = active
    }

    fun setDns(dns: String) {
        _selectedDns.value = dns
    }

    fun showAppSelection(show: Boolean) {
        _isAppSelectionVisible.value = show
    }

    fun refreshStats() {
        viewModelScope.launch {
            val blockedCount = dao.getBlockedCount()
            _stats.value = _stats.value.copy(
                adsBlocked = blockedCount,
                trackersStopped = blockedCount / 4, // Simulated ratio
                dataSaved = "${(blockedCount * 0.034).format(1)} MB"
            )
        }
    }

    fun refreshTrafficLogs() {
        viewModelScope.launch {
            _trafficLogs.value = dao.getRecentLogs()
        }
    }

    private fun Double.format(digits: Int) = "%.${digits}f".format(this)
}

data class DashboardStats(
    val adsBlocked: Int = 0,
    val trackersStopped: Int = 0,
    val dataSaved: String = "0 MB",
    val uptime: String = "0h 0m"
)

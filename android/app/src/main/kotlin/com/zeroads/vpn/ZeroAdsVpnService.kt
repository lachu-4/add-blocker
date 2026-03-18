package com.zeroads.vpn

import android.content.Intent
import android.net.VpnService
import android.os.ParcelFileDescriptor
import android.util.Log
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.ByteBuffer
import java.util.concurrent.atomic.AtomicBoolean

import android.content.Context
import android.content.pm.PackageManager

/**
 * Core VPN Service for ZeroAds.
 * Intercepts network traffic and filters DNS requests against ad-server lists.
 */
class ZeroAdsVpnService : VpnService(), Runnable {

    private var vpnInterface: ParcelFileDescriptor? = null
    private val isRunning = AtomicBoolean(false)
    private var vpnThread: Thread? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (isRunning.compareAndSet(false, true)) {
            vpnThread = Thread(this, "ZeroAdsVpnThread").apply { start() }
        }
        return START_STICKY
    }

    override fun onDestroy() {
        stopVpn()
        super.onDestroy()
    }

    private fun stopVpn() {
        isRunning.set(false)
        vpnThread?.interrupt()
        vpnInterface?.close()
        vpnInterface = null
    }

    override fun run() {
        try {
            setupVpn()
            processPackets()
        } catch (e: Exception) {
            Log.e("ZeroAdsVpn", "VPN Error", e)
        } finally {
            stopVpn()
        }
    }

    private fun setupVpn() {
        val builder = Builder()
            .setSession("ZeroAds Protection")
            .addAddress("10.0.0.2", 32)
            .addRoute("0.0.0.0", 0)
            .addDnsServer("8.8.8.8") // Default to Google DNS, can be user configured
            .setMmt(1500)
            .setBlocking(true)

        // Apply App-Level Filtering
        val prefs = getSharedPreferences("ZeroAdsPrefs", Context.MODE_PRIVATE)
        val packageManager = packageManager
        val installedApps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
        
        var allowedAppsCount = 0
        installedApps.forEach { app ->
            val isWhitelisted = prefs.getBoolean("whitelist_${app.packageName}", false)
            if (!isWhitelisted) {
                try {
                    builder.addAllowedApplication(app.packageName)
                    allowedAppsCount++
                } catch (e: PackageManager.NameNotFoundException) {
                    Log.e("ZeroAdsVpn", "App not found: ${app.packageName}")
                }
            }
        }
        
        Log.i("ZeroAdsVpn", "VPN configured for $allowedAppsCount apps")

        if (allowedAppsCount == 0) {
            Log.w("ZeroAdsVpn", "No apps selected for protection. VPN will not start.")
            stopVpn()
            return
        }

        vpnInterface = builder.establish()
        Log.i("ZeroAdsVpn", "VPN Interface established")
    }

    private fun processPackets() {
        val inputStream = FileInputStream(vpnInterface?.fileDescriptor)
        val outputStream = FileOutputStream(vpnInterface?.fileDescriptor)
        val buffer = ByteBuffer.allocate(32767)

        while (isRunning.get()) {
            val length = inputStream.read(buffer.array())
            if (length > 0) {
                buffer.limit(length)
                
                // --- PACKET FILTERING LOGIC ---
                // 1. Parse IP Header
                // 2. If UDP Port 53 (DNS), parse DNS Query
                // 3. Check domain against BlockList (Room DB)
                // 4. If blocked, drop packet or return NXDOMAIN
                // 5. If allowed, forward packet
                
                // For demonstration, we forward all packets
                // In production, we use a JNI-based C++ engine for high-speed parsing
                outputStream.write(buffer.array(), 0, length)
                buffer.clear()
            }
            
            if (Thread.interrupted()) break
        }
    }
}

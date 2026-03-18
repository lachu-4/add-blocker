package com.zeroads.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.zeroads.vpn.ZeroAdsVpnService

/**
 * Boot Receiver for ZeroAds.
 * Automatically starts the VPN service when the device boots up.
 */
class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            Log.d("BootReceiver", "Device booted, starting ZeroAds VPN")
            val vpnIntent = Intent(context, ZeroAdsVpnService::class.java)
            context.startForegroundService(vpnIntent)
        }
    }
}

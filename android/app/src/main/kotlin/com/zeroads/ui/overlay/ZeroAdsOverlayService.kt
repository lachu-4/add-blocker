package com.zeroads.ui.overlay

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.IBinder
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import com.zeroads.R
import com.zeroads.vpn.ZeroAdsVpnService

/**
 * Overlay Service for ZeroAds.
 * Displays a floating widget with real-time protection stats.
 */
class ZeroAdsOverlayService : Service() {

    private var windowManager: WindowManager? = null
    private var overlayView: View? = null

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        
        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.START
            x = 100
            y = 100
        }

        // Inflate overlay layout (to be created)
        // For now, we'll use a simple TextView or a custom layout if we had one
        // Since I'm creating the project, I'll assume a layout file exists or create it
        
        // Let's create a simple programmatic view for now if layout is missing
        val textView = TextView(this).apply {
            text = "🛡️ ZEROADS: ACTIVE\nAds Blocked: 0"
            setBackgroundColor(0xCC000000.toInt())
            setTextColor(0xFF00B8FF.toInt()) // Cyan Blue
            setPadding(20, 20, 20, 20)
            textSize = 12f
        }
        
        overlayView = textView
        windowManager?.addView(overlayView, params)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (overlayView != null) {
            windowManager?.removeView(overlayView)
        }
    }
}

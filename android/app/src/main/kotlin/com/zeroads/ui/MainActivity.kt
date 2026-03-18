package com.zeroads.ui

import android.content.Intent
import android.net.VpnService
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.zeroads.ui.screens.MainScreen
import com.zeroads.ui.theme.ZeroAdsTheme
import com.zeroads.vpn.ZeroAdsVpnService

/**
 * Main Activity for ZeroAds.
 * Handles VPN permissions and service lifecycle.
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            ZeroAdsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(
                        onToggleProtection = { isActive ->
                            if (isActive) {
                                startVpn()
                            } else {
                                stopVpn()
                            }
                        }
                    )
                }
            }
        }
    }

    private fun startVpn() {
        val intent = VpnService.prepare(this)
        if (intent != null) {
            startActivityForResult(intent, 0)
        } else {
            onActivityResult(0, RESULT_OK, null)
        }
    }

    private fun stopVpn() {
        stopService(Intent(this, ZeroAdsVpnService::class.java))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            val intent = Intent(this, ZeroAdsVpnService::class.java)
            startService(intent)
        }
    }
}

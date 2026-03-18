package com.zeroads

import android.content.Intent
import android.net.VpnService
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.zeroads.ui.screens.DashboardScreen
import com.zeroads.ui.theme.ZeroAdsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            ZeroAdsTheme {
                var isProtected by remember { mutableStateOf(false) }
                
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DashboardScreen(
                        isProtected = isProtected,
                        onToggleProtection = { active ->
                            if (active) {
                                prepareVpn()
                            } else {
                                stopVpn()
                            }
                            isProtected = active
                        },
                        blockedCount = 1248,
                        dataSaved = "42.5 MB"
                    )
                }
            }
        }
    }

    private fun prepareVpn() {
        val intent = VpnService.prepare(this)
        if (intent != null) {
            startActivityForResult(intent, 0)
        } else {
            onActivityResult(0, RESULT_OK, null)
        }
    }

    private fun stopVpn() {
        // Logic to stop the service
        stopService(Intent(this, com.zeroads.vpn.ZeroAdsVpnService::class.java))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            startService(Intent(this, com.zeroads.vpn.ZeroAdsVpnService::class.java))
        }
    }
}

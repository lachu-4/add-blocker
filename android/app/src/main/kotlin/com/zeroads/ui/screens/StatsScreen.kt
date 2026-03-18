package com.zeroads.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.zeroads.ui.viewmodel.DashboardViewModel

@Composable
fun StatsScreen(
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val stats by viewModel.stats.collectAsState()
    val trafficLogs by viewModel.trafficLogs.collectAsState()

    val cyberCyan = Color(0xFF00B8FF)
    val cyberDark = Color(0xFF0A0A0A)
    val cyberSurface = Color(0xFF121212)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(cyberDark)
            .padding(16.dp)
    ) {
        Text(
            "STATISTICS",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            letterSpacing = 1.sp
        )
        
        Spacer(modifier = Modifier.height(24.dp))

        // Detailed Stats Grid
        Row(modifier = Modifier.fillMaxWidth()) {
            StatCard(
                label = "Ads Blocked",
                value = stats.adsBlocked.toString(),
                icon = Icons.Default.Shield,
                color = cyberCyan,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(12.dp))
            StatCard(
                label = "Data Saved",
                value = stats.dataSaved,
                icon = Icons.Default.Bolt,
                color = Color(0xFF0070FF),
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            StatCard(
                label = "Trackers",
                value = stats.trackersStopped.toString(),
                icon = Icons.Default.Lock,
                color = Color(0xFFA855F7),
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(12.dp))
            StatCard(
                label = "Uptime",
                value = stats.uptime,
                icon = Icons.Default.Timer,
                color = Color(0xFFF97316),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            "RECENT TRAFFIC LOGS",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray,
            letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.height(12.dp))
        
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clip(RoundedCornerShape(16.dp))
                .background(cyberSurface)
                .border(1.dp, Color.White.withAlpha(0.05f), RoundedCornerShape(16.dp))
        ) {
            if (trafficLogs.isEmpty()) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                        Text("No traffic detected yet.", color = Color.Gray, fontSize = 12.sp)
                    }
                }
            } else {
                items(trafficLogs) { log ->
                    TrafficLogItem(log.domain, log.type, cyberCyan)
                }
            }
        }
    }
}

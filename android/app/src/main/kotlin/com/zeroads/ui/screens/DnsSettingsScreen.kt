package com.zeroads.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.filled.RadioButtonUnchecked
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
fun DnsSettingsScreen(
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val cyberCyan = Color(0xFF00B8FF)
    val cyberDark = Color(0xFF0A0A0A)
    val cyberSurface = Color(0xFF121212)

    val selectedDns by viewModel.selectedDns.collectAsState()

    val dnsProviders = listOf(
        DnsProvider("AdGuard DNS", "94.140.14.14", "Blocks ads, trackers, and phishing."),
        DnsProvider("Cloudflare", "1.1.1.1", "Fast and private DNS resolver."),
        DnsProvider("Google DNS", "8.8.8.8", "Reliable and widely used."),
        DnsProvider("Quad9", "9.9.9.9", "Security-focused DNS with malware blocking."),
        DnsProvider("CleanBrowsing", "185.228.168.9", "Family-friendly DNS filtering.")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(cyberDark)
            .padding(16.dp)
    ) {
        Text(
            "DNS SETTINGS",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            letterSpacing = 1.sp
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            "Choose a DNS provider to enhance your protection and speed.",
            fontSize = 12.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(24.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clip(RoundedCornerShape(16.dp))
                .background(cyberSurface)
                .border(1.dp, Color.White.withAlpha(0.05f), RoundedCornerShape(16.dp))
        ) {
            items(dnsProviders) { provider ->
                DnsProviderItem(
                    provider = provider,
                    isSelected = selectedDns == provider.name,
                    onSelect = { viewModel.setDns(it) },
                    cyberCyan = cyberCyan
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(
            onClick = { /* Apply DNS settings */ },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = cyberCyan, contentColor = Color.Black),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("APPLY DNS SETTINGS", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun DnsProviderItem(
    provider: DnsProvider,
    isSelected: Boolean,
    onSelect: (String) -> Unit,
    cyberCyan: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect(provider.name) }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
            Icon(
                imageVector = Icons.Default.Dns,
                contentDescription = null,
                tint = if (isSelected) cyberCyan else Color.Gray,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(provider.name, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Text(provider.ip, fontSize = 12.sp, color = cyberCyan.copy(alpha = 0.7f))
                Text(provider.description, fontSize = 10.sp, color = Color.Gray)
            }
        }
        Icon(
            imageVector = if (isSelected) Icons.Default.RadioButtonChecked else Icons.Default.RadioButtonUnchecked,
            contentDescription = null,
            tint = if (isSelected) cyberCyan else Color.Gray,
            modifier = Modifier.size(24.dp)
        )
    }
}

data class DnsProvider(val name: String, val ip: String, val description: String)

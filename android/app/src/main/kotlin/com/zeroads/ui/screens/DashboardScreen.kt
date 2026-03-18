package com.zeroads.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.hilt.navigation.compose.hiltViewModel
import com.zeroads.ui.viewmodel.DashboardViewModel

import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.graphics.drawable.toBitmap

/**
 * Dashboard Screen for ZeroAds.
 * Modern cyber security dashboard with real-time stats and traffic logs.
 */
@Composable
fun DashboardScreen(
    onToggleProtection: (Boolean) -> Unit,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val isProtected by viewModel.isProtected.collectAsState()
    val stats by viewModel.stats.collectAsState()
    val trafficLogs by viewModel.trafficLogs.collectAsState()
    val installedApps by viewModel.installedApps.collectAsState()
    val isAppSelectionVisible by viewModel.isAppSelectionVisible.collectAsState()

    val cyberCyan = Color(0xFF00B8FF)
    val cyberDark = Color(0xFF0A0A0A)
    val cyberSurface = Color(0xFF121212)

    val isAppsLoading by viewModel.isAppsLoading.collectAsState()
    val showSystemApps by viewModel.showSystemApps.collectAsState()

    if (isAppSelectionVisible) {
        AppSelectionDialog(
            apps = installedApps,
            isLoading = isAppsLoading,
            showSystemApps = showSystemApps,
            onDismiss = { viewModel.showAppSelection(false) },
            onConfirm = {
                viewModel.showAppSelection(false)
                viewModel.setProtection(true)
                onToggleProtection(true)
            },
            onToggleApp = { packageName, isSelected ->
                viewModel.toggleAppSelection(packageName, isSelected)
            },
            onToggleSystemApps = { viewModel.toggleSystemApps(it) },
            onSelectAll = { viewModel.selectAll(it) },
            onSearch = { viewModel.setSearchQuery(it) },
            cyberCyan = cyberCyan
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(cyberDark)
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "ZEROADS",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    letterSpacing = 2.sp
                )
                Text(
                    text = "System-Wide Protection",
                    fontSize = 10.sp,
                    color = cyberCyan.copy(alpha = 0.7f),
                    letterSpacing = 1.sp
                )
            }
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Profile",
                tint = cyberCyan,
                modifier = Modifier.size(32.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Protection Status Card
        ProtectionStatusCard(
            isProtected = isProtected,
            onToggle = { 
                if (!isProtected) {
                    if (viewModel.hasProtectedApps()) {
                        viewModel.setProtection(true)
                        onToggleProtection(true)
                    } else {
                        viewModel.showAppSelection(true)
                    }
                } else {
                    viewModel.setProtection(false)
                    onToggleProtection(false)
                }
            },
            onManageApps = { viewModel.showAppSelection(true) },
            cyberCyan = cyberCyan
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Stats Grid
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

        // Live Traffic Section
        Text(
            text = "LIVE TRAFFIC",
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

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.text.input.ImeAction

@Composable
fun AppSelectionDialog(
    apps: List<com.zeroads.data.manager.AppInfo>,
    isLoading: Boolean,
    showSystemApps: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    onToggleApp: (String, Boolean) -> Unit,
    onToggleSystemApps: (Boolean) -> Unit,
    onSelectAll: (Boolean) -> Unit,
    onSearch: (String) -> Unit,
    cyberCyan: Color
) {
    var searchQuery by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF121212),
        title = {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "SELECT APPS",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        letterSpacing = 1.sp
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("System", fontSize = 10.sp, color = Color.Gray)
                        Switch(
                            checked = showSystemApps,
                            onCheckedChange = onToggleSystemApps,
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = cyberCyan,
                                checkedTrackColor = cyberCyan.copy(alpha = 0.5f)
                            ),
                            modifier = Modifier.scale(0.6f)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { 
                        searchQuery = it
                        onSearch(it)
                    },
                    placeholder = { Text("Search apps...", color = Color.Gray, fontSize = 12.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = cyberCyan) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = cyberCyan,
                        unfocusedBorderColor = Color.Gray.copy(alpha = 0.3f),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search)
                )
            }
        },
        text = {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Choose apps to block ads in.",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    TextButton(onClick = { onSelectAll(true) }) {
                        Text("SELECT ALL", color = cyberCyan, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
                
                if (isLoading) {
                    Box(modifier = Modifier.fillMaxWidth().height(300.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = cyberCyan)
                    }
                } else {
                    LazyColumn(modifier = Modifier.height(300.dp)) {
                        items(apps) { app ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                                    .clickable { onToggleApp(app.packageName, !app.isWhitelisted) },
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                    Image(
                                        bitmap = app.icon.toBitmap().asImageBitmap(),
                                        contentDescription = null,
                                        modifier = Modifier.size(32.dp)
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(app.appName, fontSize = 14.sp, color = Color.White, maxLines = 1)
                                        Text(app.packageName, fontSize = 10.sp, color = Color.Gray, maxLines = 1)
                                    }
                                }
                                Checkbox(
                                    checked = !app.isWhitelisted,
                                    onCheckedChange = { onToggleApp(app.packageName, it) },
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = cyberCyan,
                                        uncheckedColor = Color.Gray
                                    )
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = cyberCyan, contentColor = Color.Black),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("START PROTECTION", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("CANCEL", color = Color.Gray)
            }
        }
    )
}

@Composable
fun ProtectionStatusCard(isProtected: Boolean, onToggle: () -> Unit, onManageApps: () -> Unit, cyberCyan: Color) {
    val scale by animateFloatAsState(
        targetValue = if (isProtected) 1.05f else 1f,
        animationSpec = tween(durationMillis = 2000)
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White.withAlpha(0.03f))
            .border(1.dp, if (isProtected) cyberCyan.withAlpha(0.3f) else Color.Gray.withAlpha(0.3f), RoundedCornerShape(24.dp))
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .scale(scale)
                    .border(2.dp, if (isProtected) cyberCyan else Color.Gray, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isProtected) Icons.Default.ShieldCheck else Icons.Default.ShieldAlert,
                    contentDescription = "Shield",
                    tint = if (isProtected) cyberCyan else Color.Gray,
                    modifier = Modifier.size(64.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = if (isProtected) "PROTECTION ACTIVE" else "PROTECTION PAUSED",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = if (isProtected) cyberCyan else Color.Gray
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = if (isProtected) "Your device is shielded from ads and trackers." else "Enable protection to block system-wide ads.",
                fontSize = 12.sp,
                color = Color.Gray,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Row(modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = onToggle,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isProtected) cyberCyan else Color.DarkGray,
                        contentColor = if (isProtected) Color.Black else Color.White
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = if (isProtected) "STOP" else "START",
                        fontWeight = FontWeight.Bold
                    )
                }
                
                Spacer(modifier = Modifier.width(12.dp))
                
                OutlinedButton(
                    onClick = onManageApps,
                    border = BorderStroke(1.dp, Color.Gray.copy(alpha = 0.5f)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("MANAGE APPS", fontSize = 10.sp, color = Color.White)
                }
            }
        }
    }
}


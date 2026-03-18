package com.zeroads.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.collectAsState
import com.zeroads.ui.viewmodel.DashboardViewModel

@Composable
fun AppControlScreen(
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val installedApps by viewModel.installedApps.collectAsState()
    val isAppsLoading by viewModel.isAppsLoading.collectAsState()
    val showSystemApps by viewModel.showSystemApps.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    val cyberCyan = Color(0xFF00B8FF)
    val cyberDark = Color(0xFF0A0A0A)
    val cyberSurface = Color(0xFF121212)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(cyberDark)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "APP CONTROL",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                letterSpacing = 1.sp
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("System", fontSize = 10.sp, color = Color.Gray)
                Switch(
                    checked = showSystemApps,
                    onCheckedChange = { viewModel.toggleSystemApps(it) },
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
                viewModel.setSearchQuery(it)
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

        Spacer(modifier = Modifier.height(16.dp))

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
            TextButton(onClick = { viewModel.selectAll(true) }) {
                Text("SELECT ALL", color = cyberCyan, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clip(RoundedCornerShape(16.dp))
                .background(cyberSurface)
                .border(1.dp, Color.White.withAlpha(0.05f), RoundedCornerShape(16.dp))
        ) {
            if (isAppsLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = cyberCyan
                )
            } else if (installedApps.isEmpty()) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("No apps found.", color = Color.Gray, fontSize = 14.sp)
                    if (searchQuery.isNotEmpty()) {
                        Text("Try a different search query.", color = Color.Gray.copy(alpha = 0.7f), fontSize = 12.sp)
                    }
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(installedApps, key = { it.packageName }) { app ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp)
                                .clickable { viewModel.toggleAppSelection(app.packageName, !app.isWhitelisted) },
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                Image(
                                    bitmap = app.icon.toBitmap().asImageBitmap(),
                                    contentDescription = null,
                                    modifier = Modifier.size(40.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(app.appName, fontSize = 14.sp, color = Color.White, maxLines = 1)
                                    Text(app.packageName, fontSize = 10.sp, color = Color.Gray, maxLines = 1)
                                }
                            }
                            Checkbox(
                                checked = !app.isWhitelisted,
                                onCheckedChange = { viewModel.toggleAppSelection(app.packageName, it) },
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
    }
}

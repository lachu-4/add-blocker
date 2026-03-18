package com.zeroads.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun StatCard(label: String, value: String, icon: ImageVector, color: Color, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White.withAlpha(0.03f))
            .border(1.dp, Color.White.withAlpha(0.05f), RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Column {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = color,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = label, fontSize = 10.sp, color = Color.Gray, letterSpacing = 1.sp)
            Text(text = value, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = color)
        }
    }
}

@Composable
fun TrafficLogItem(domain: String, type: String, cyberCyan: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(cyberCyan)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = domain, fontSize = 12.sp, color = Color.White.copy(alpha = 0.8f))
        }
        Row {
            Text(text = type, fontSize = 10.sp, color = Color.Gray)
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = "Blocked", fontSize = 10.sp, color = cyberCyan.copy(alpha = 0.7f))
        }
    }
}

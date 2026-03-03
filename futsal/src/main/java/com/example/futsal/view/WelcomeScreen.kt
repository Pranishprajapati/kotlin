package com.example.futsal.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.material.icons.filled.Groups
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Dashing Color Palette
val DeepBlack = Color(0xFF0B0E11)
val MidnightBlue = Color(0xFF161B22)
val NeonBlue = Color(0xFF00D2FF)
val ElectricCyan = Color(0xFF3A7BD5)
val GlassWhite = Color(0xFFFFFFFF).copy(alpha = 0.05f)
val GlassBorder = Color(0xFFFFFFFF).copy(alpha = 0.1f)

@Composable
fun WelcomeScreen(onNavigate: (String) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(MidnightBlue, DeepBlack)
                )
            )
    ) {
        // Decorative Background Glow
        Box(
            modifier = Modifier
                .size(300.dp)
                .offset(x = (-100).dp, y = (-50).dp)
                .background(NeonBlue.copy(alpha = 0.15f), CircleShape)
                .blur(100.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))

            // Dashing Branding
            Surface(
                color = NeonBlue.copy(alpha = 0.1f),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.padding(bottom = 8.dp)
            ){}

            Text(
                text = "FUTSILO",
                color = Color.White,
                fontSize = 42.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 2.sp
            )
            
            Text(
                text = "The Future of Futsal Booking",
                color = Color.White.copy(alpha = 0.6f),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Glassmorphic Feature Card 1
            DashingFeatureCard(
                title = "Instant Ground Booking",
                description = "Reserve top-rated futsal courts in Kathmandu in just a few taps.",
                icon = Icons.Default.ElectricBolt,
                accentColor = NeonBlue
            )

            Spacer(modifier = Modifier.height(20.dp))

// Glassmorphic Feature Card 2
            DashingFeatureCard(
                title = "Real-Time Slot Availability",
                description = "See live time slots and avoid double bookings instantly.",
                icon = Icons.Default.NotificationsActive,
                accentColor = Color(0xFFBB86FC)
            )

            Spacer(modifier = Modifier.height(20.dp))

// Glassmorphic Feature Card 3 (NEW)
            DashingFeatureCard(
                title = "Team & Match Management",
                description = "Create squads, schedule matches, and manage players effortlessly.",
                icon = Icons.Default.Groups,
                accentColor = Color(0xFF00FFA3)
            )

            Spacer(modifier = Modifier.weight(1f))

            // Action Buttons
            Button(
                onClick = { onNavigate("login") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(ElectricCyan, NeonBlue)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "GET STARTED", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = { onNavigate("register") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, GlassBorder),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
            ) {
                Text(text = "CREATE ACCOUNT", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "v2.0.4 Futsilo",
                fontSize = 10.sp,
                color = Color.White.copy(alpha = 0.3f),
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun DashingFeatureCard(
    title: String,
    description: String,
    icon: ImageVector,
    accentColor: Color
) {
    Surface(
        color = GlassWhite,
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(1.dp, GlassBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                color = accentColor.copy(alpha = 0.2f),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.size(52.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.padding(12.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = description,
                    color = Color.White.copy(alpha = 0.5f),
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )
            }
        }
    }
}

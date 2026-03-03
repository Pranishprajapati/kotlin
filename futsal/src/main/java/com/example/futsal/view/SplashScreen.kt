package com.example.futsal.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.futsal.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onNavigate: (String) -> Unit) {
    // Navigate after 1.2 seconds
    LaunchedEffect(true) {
        delay(1350)
        onNavigate("login")
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Background image, scaled to fill the whole screen
        Image(
            painter = painterResource(id = R.drawable.futsal),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Semi-transparent overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.55f))
        )

        // Centered text
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "FUTSAL ARENA",
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Play • Book • Compete",
                fontSize = 16.sp,
                color = Color.White.copy(alpha = 0.8f)
            )
        }
    }
}
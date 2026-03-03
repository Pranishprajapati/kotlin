package com.example.futsal.view

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.futsal.R
import com.example.futsal.model.FutsalGround
import com.example.futsal.model.futsalGroundsList

// --- DASHING COLORS ---
val DarkBg = Color(0xFF0F172A)
val CardBg = Color(0xFF1E293B)
val PrimaryGreen = Color(0xFF22C55E)
val TextGray = Color(0xFF94A3B8)

@Composable
fun GroundsScreen(onBookNow: (FutsalGround) -> Unit) {
    var selectedGround by remember { mutableStateOf<FutsalGround?>(null) }

    Crossfade(targetState = selectedGround, label = "") { ground ->
        if (ground == null) {
            GroundsList(onGroundSelected = { selectedGround = it })
        } else {
            GroundDetailsScreen(
                ground = ground,
                onBack = { selectedGround = null },
                onProceedBooking = { onBookNow(ground) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroundsList(onGroundSelected: (FutsalGround) -> Unit) {
    Scaffold(
        containerColor = DarkBg,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Available Grounds", color = Color.White, fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = DarkBg),
                windowInsets = WindowInsets(0, 0, 0, 0)
            )
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding).fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(futsalGroundsList) { ground ->
                GroundCardV2(ground, onClick = { onGroundSelected(ground) })
            }
        }
    }
}

@Composable
fun GroundCardV2(ground: FutsalGround, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardBg)
    ) {
        Column {
            Box {
                Image(
                    painter = painterResource(id = ground.imageRes),
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth().height(160.dp),
                    contentScale = ContentScale.Crop
                )
                Surface(
                    modifier = Modifier.padding(12.dp).align(Alignment.TopStart),
                    color = PrimaryGreen,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Available Today", color = Color.White, fontSize = 10.sp, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                }
            }
            Column(modifier = Modifier.padding(16.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(ground.name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text("Rs ${ground.price}", color = Color.White, fontWeight = FontWeight.Bold)
                }
                Text(text = ground.location, color = TextGray, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, null, tint = Color(0xFFFFB300), modifier = Modifier.size(16.dp))
                    Text(" ${ground.rating}", color = TextGray, fontSize = 12.sp)
                    Spacer(modifier = Modifier.weight(1f))
                    Button(
                        onClick = onClick,
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.height(32.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp)
                    ) {
                        Text("Book Now >", fontSize = 12.sp, color = Color.White)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun GroundDetailsScreen(ground: FutsalGround, onBack: () -> Unit, onProceedBooking: () -> Unit) {
    Scaffold(containerColor = DarkBg) { padding ->
        LazyColumn(modifier = Modifier.fillMaxSize().padding(padding)) {
            item {
                Box(modifier = Modifier.fillMaxWidth().height(280.dp)) {
                    Image(
                        painter = painterResource(id = ground.imageRes),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth().statusBarsPadding().padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        IconButton(onClick = onBack, modifier = Modifier.background(Color.Black.copy(0.3f), RoundedCornerShape(12.dp))) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White)
                        }
                        IconButton(onClick = {}, modifier = Modifier.background(Color.Black.copy(0.3f), RoundedCornerShape(12.dp))) {
                            Icon(Icons.Default.FavoriteBorder, null, tint = Color.White)
                        }
                    }
                }
            }

            item {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column {
                            Text(ground.name, color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                            Text(ground.location, color = TextGray, fontSize = 14.sp)
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 4.dp)) {
                                Icon(Icons.Default.Star, null, tint = Color(0xFFFFB300), modifier = Modifier.size(16.dp))
                                Text(" ${ground.rating}", color = TextGray, fontSize = 14.sp)
                            }
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("Rs ${ground.price}", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            Text("per hour", color = TextGray, fontSize = 12.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                    Text("Facilities", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ground.facilities.forEach { facility ->
                            Surface(color = CardBg, shape = RoundedCornerShape(8.dp)) {
                                Text(facility, color = Color.White, fontSize = 12.sp, modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = onProceedBooking,
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Book Now", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
        }
    }
}

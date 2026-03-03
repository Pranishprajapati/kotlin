@file:OptIn(ExperimentalMaterial3Api::class)
package com.example.futsal.view

import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.*
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
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.futsal.model.FutsalGround
import com.example.futsal.model.futsalGroundsList
import com.example.futsal.repository.UserRepoImpl
import com.example.futsal.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth

// ------------------- THEME COLORS -------------------
val MidnightBlue_Main = Color(0xFF161B22)
val DeepBlack_Main = Color(0xFF0B0E11)
val NeonBlue_Main = Color(0xFF00D2FF)
val GlassWhite_Main = Color(0xFFFFFFFF).copy(alpha = 0.05f)
val GlassBorder_Main = Color(0xFFFFFFFF).copy(alpha = 0.1f)
val TextGray_Main = Color(0xFF94A3B8)

// ------------------- MAIN SCREEN -------------------
@Composable
fun MainScreen(onLogout: () -> Unit) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    var selectedGroundForBooking by remember { mutableStateOf<FutsalGround?>(null) }

    val tabs = listOf("Home", "Grounds", "Bookings", "Profile")
    val tabIcons = listOf(Icons.Default.Home, Icons.Default.SportsSoccer, Icons.Default.DateRange, Icons.Default.Person)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(colors = listOf(MidnightBlue_Main, DeepBlack_Main)))
    ) {
        // Top decorative glow
        Box(
            modifier = Modifier
                .size(300.dp)
                .align(Alignment.TopEnd)
                .offset(x = 100.dp, y = (-50).dp)
                .background(NeonBlue_Main.copy(alpha = 0.1f), CircleShape)
                .blur(100.dp)
        )

        if (selectedGroundForBooking != null) {
            BookingScreen(
                groundName = selectedGroundForBooking!!.name,
                price = selectedGroundForBooking!!.price,
                onBack = { selectedGroundForBooking = null }
            )
        } else {
            Scaffold(
                containerColor = Color.Transparent,
                bottomBar = {
                    DashingBottomBar(
                        tabs = tabs,
                        icons = tabIcons,
                        selectedIndex = selectedTabIndex
                    ) { selectedTabIndex = it }
                }
            ) { paddingValues ->
                Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
                    when (selectedTabIndex) {
                        0 -> HomeTab(onBookNow = { selectedGroundForBooking = it }, onLogout = onLogout)
                        1 -> GroundsScreen(onBookNow = { selectedGroundForBooking = it })
                        2 -> BookingsTab()
                        3 -> ProfileScreen(onLogout = onLogout)
                    }
                }
            }
        }
    }
}

// ------------------- HOME TAB -------------------
@Composable
fun HomeTab(onBookNow: (FutsalGround) -> Unit, onLogout: () -> Unit) {
    var searchQuery by remember { mutableStateOf("") }
    val userViewModel = remember { UserViewModel(UserRepoImpl()) }
    val currentUser = FirebaseAuth.getInstance().currentUser

    var userName by remember { mutableStateOf("Admin") } // you can change as needed
    var userAddress by remember { mutableStateOf("Loading...") }

    LaunchedEffect(currentUser) {
        currentUser?.let { user ->
            userViewModel.getUserData(user.uid) { model ->
                model?.let {
                    Handler(Looper.getMainLooper()).post {
                        userName = it.name
                        userAddress = it.address.ifEmpty { "Address not set" }
                    }
                }
            }
        }
    }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Hi, $userName,", color = TextGray_Main, fontSize = 18.sp)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocationOn, null, tint = NeonBlue_Main, modifier = Modifier.size(14.dp))
                            Text(" $userAddress", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                    }

                    // Logout icon button
                    IconButton(
                        onClick = { onLogout() },
                        modifier = Modifier.background(GlassWhite_Main, CircleShape)
                    ) {
                        Icon(Icons.Default.Logout, contentDescription = "Logout", tint = Color.Red)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search...", color = Color.White.copy(alpha = 0.4f)) },
                    leadingIcon = { Icon(Icons.Default.Search, null, tint = NeonBlue_Main) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedContainerColor = GlassWhite_Main,
                        unfocusedContainerColor = GlassWhite_Main,
                        focusedBorderColor = NeonBlue_Main,
                        unfocusedBorderColor = GlassBorder_Main,
                        cursorColor = NeonBlue_Main
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))
                Text("Popular Grounds", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        // Display only 4 popular grounds
        items(futsalGroundsList.filter { it.name.contains(searchQuery, true) }.take(4)) { ground ->
            Box(modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)) {
                DashingHomeCard(ground = ground, onClick = { onBookNow(ground) })
            }
        }

        item { Spacer(modifier = Modifier.height(20.dp)) }
    }
}

// ------------------- DASHING HOME CARD -------------------
@Composable
fun DashingHomeCard(ground: FutsalGround, onClick: () -> Unit) {
    Surface(
        color = GlassWhite_Main,
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(1.dp, GlassBorder_Main),
        modifier = Modifier.fillMaxWidth().clickable { onClick() }
    ) {
        Column {
            Box {
                Image(
                    painterResource(ground.imageRes),
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth().height(160.dp),
                    contentScale = ContentScale.Crop
                )
                Surface(
                    modifier = Modifier.padding(12.dp).align(Alignment.TopStart),
                    color = NeonBlue_Main,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        "Available Today",
                        color = DeepBlack_Main,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(ground.name, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Star, null, tint = Color.Yellow, modifier = Modifier.size(16.dp))
                        Text(" ${ground.rating}", color = Color.White, fontSize = 14.sp)
                    }
                }
                Text(ground.location, color = Color.White.copy(alpha = 0.5f), fontSize = 12.sp)
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onClick,
                    colors = ButtonDefaults.buttonColors(containerColor = NeonBlue_Main),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().height(48.dp)
                ) {
                    Text("BOOK NOW", color = DeepBlack_Main, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// ------------------- BOTTOM BAR -------------------
@Composable
fun DashingBottomBar(
    tabs: List<String>,
    icons: List<ImageVector>,
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit
) {
    NavigationBar(containerColor = DeepBlack_Main, contentColor = NeonBlue_Main) {
        tabs.forEachIndexed { index, title ->
            NavigationBarItem(
                icon = { Icon(icons[index], contentDescription = title) },
                label = { Text(title, fontSize = 10.sp) },
                selected = selectedIndex == index,
                onClick = { onTabSelected(index) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = NeonBlue_Main,
                    unselectedIconColor = Color.White.copy(alpha = 0.4f),
                    indicatorColor = NeonBlue_Main.copy(alpha = 0.1f)
                )
            )
        }
    }
}
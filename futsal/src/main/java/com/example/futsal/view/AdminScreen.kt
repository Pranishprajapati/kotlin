@file:OptIn(ExperimentalMaterial3Api::class)
package com.example.futsal.view

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
import com.example.futsal.model.BookingModel
import com.example.futsal.model.FutsalGround
import com.example.futsal.viewmodel.AdminViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.request.colorSpace

// ---- THEME COLORS ----
val MidnightBlue_Admin = Color(0xFF161B22)
val DeepBlack_Admin = Color(0xFF0B0E11)
val NeonBlue_Admin = Color(0xFF00D2FF)
val GlassWhite_Admin = Color(0xFFFFFFFF).copy(alpha = 0.05f)
val GlassBorder_Admin = Color(0xFFFFFFFF).copy(alpha = 0.1f)
val PrimaryGreen_Admin = Color(0xFF22C55E)
val TextGray_Admin = Color(0xFF94A3B8)

val DarkCard = Color(0xFFFFFFFF)



// ----------------- ADMIN MAIN SCREEN -----------------
@Composable
fun AdminMainScreen(
    onLogout: () -> Unit,
    viewModel: AdminViewModel = viewModel()
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    var selectedGroundForBooking by remember { mutableStateOf<FutsalGround?>(null) }

    val tabs = listOf("Home", "Grounds", "Bookings", "Profile")
    val tabIcons = listOf(Icons.Default.Home, Icons.Default.SportsSoccer, Icons.Default.DateRange, Icons.Default.Person)
    var bookings by remember { mutableStateOf(listOf<BookingModel>()) }

    LaunchedEffect(Unit) {
        viewModel.getAllBookings { bookings = it }
    }

    Box(
        modifier = Modifier.fillMaxSize()
            .background(Brush.verticalGradient(colors = listOf(MidnightBlue_Admin, DeepBlack_Admin)))
    ) {
        // Decorative glow
        Box(
            modifier = Modifier.size(300.dp).align(Alignment.TopEnd).offset(x = 100.dp, y = (-50).dp)
                .background(NeonBlue_Admin.copy(alpha = 0.1f), CircleShape).blur(100.dp)
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
                bottomBar = { AdminBottomBar(tabs, tabIcons, selectedTabIndex) { selectedTabIndex = it } }
            ) { padding ->
                Box(modifier = Modifier.padding(padding).fillMaxSize()) {
                    when (selectedTabIndex) {
                        0 -> AdminHomeTab(viewModel.grounds)
                        1 -> AdminGroundScreen(
                            grounds = viewModel.grounds,
                            onSaveGround = { viewModel.addOrUpdateGround(it) }
                        )
                        2 -> AdminBookingsTab(viewModel)
                        3 -> AdminProfileScreen(onLogout)
                    }
                }
            }
        }
    }
}

// ----------------- ADMIN HOME TAB -----------------
@Composable
fun AdminHomeTab(grounds: List<FutsalGround>) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(rememberScrollState())
    ) {

        Text(
            text = "Admin Dashboard",
            color = DarkCard,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "All Available Grounds",
            color = DarkCard,
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        grounds.forEach { ground ->

            Surface(
                color = DeepBlack,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {

                Column {
                    Image(
                        painter = painterResource(id = ground.imageRes),
                        contentDescription = ground.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp),
                        contentScale = ContentScale.Crop
                    )

                    Column(modifier = Modifier.padding(16.dp)) {

                        Text(
                            text = ground.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = DarkCard
                        )

                        Text(
                            text = ground.location,
                            color = DarkCard
                        )

                        Text(
                            text = "Rs ${ground.price}",
                            color = DarkCard,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}
// ----------------- ADMIN BOOKINGS TAB -----------------
@Composable
fun AdminBookingsTab(viewModel: AdminViewModel) {
    var bookings by remember { mutableStateOf(listOf<BookingModel>()) }

    LaunchedEffect(Unit) {
        viewModel.getAllBookings { bookings = it }
    }

    LazyColumn(
        contentPadding = PaddingValues(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(bookings) { booking ->
            BookingAdminCard(
                booking = booking,
                onStatusChange = { newStatus -> viewModel.updateBookingStatus(booking.id, newStatus) }
            )
        }
    }
}

// ----------------- ADMIN BOOKING CARD -----------------
@Composable
fun BookingAdminCard(
    booking: BookingModel,
    onStatusChange: (String) -> Unit
) {
    var currentStatus by remember { mutableStateOf(booking.status ?: "Pending") }

    Surface(
        color = GlassWhite_Admin,
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, GlassBorder_Admin),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(booking.groundName, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text("Slot: ${booking.timeSlot}", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp, )
            Text("Date: ${booking.date}", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
            Text("Price: ${booking.price}", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(
                    onClick = {
                        currentStatus = "Accepted"
                        onStatusChange("Accepted")
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (currentStatus == "Accepted") Color.Gray else PrimaryGreen_Admin
                    ),
                    enabled = currentStatus != "Accepted"
                ) {
                    Text("ACCEPT", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }

                OutlinedButton(
                    onClick = {
                        currentStatus = "Declined"
                        onStatusChange("Declined")
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Color.Red.copy(alpha = 0.5f)),
                    enabled = currentStatus != "Declined"
                ) {
                    Text("DECLINE", color = Color.Red, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }
        }
    }
}

// ----------------- ADMIN BOTTOM BAR -----------------
@Composable
fun AdminBottomBar(
    tabs: List<String>,
    icons: List<ImageVector>,
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit
) {
    NavigationBar(containerColor = DeepBlack_Admin, contentColor = NeonBlue_Admin) {
        tabs.forEachIndexed { index, title ->
            NavigationBarItem(
                icon = { Icon(icons[index], title) },
                label = { Text(title, fontSize = 10.sp) },
                selected = selectedIndex == index,
                onClick = { onTabSelected(index) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = NeonBlue_Admin,
                    unselectedIconColor = Color.White.copy(alpha = 0.4f),
                    indicatorColor = NeonBlue_Admin.copy(alpha = 0.1f)
                )
            )
        }
    }
}

@Composable
fun AdminGroundScreen(
    grounds: List<FutsalGround>,
    onSaveGround: (FutsalGround) -> Unit
) {
    var editingGround by remember { mutableStateOf<FutsalGround?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(rememberScrollState())

    ) {

        Text("Manage Grounds", fontSize = 22.sp, color = DarkCard)

        Spacer(modifier = Modifier.height(16.dp))

        // Existing Grounds
        grounds.forEach { ground ->
            GroundCard(ground = ground) {
                editingGround = it
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                editingGround = FutsalGround(
                    id = "",
                    name = "",
                    location = "",
                    distance = "",
                    rating = 0.0,
                    reviewCount = 0,
                    price = 0,
                    facilities = emptyList(),
                    imageRes = 0,
                    description = ""
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add New Ground")
        }

        Spacer(modifier = Modifier.height(20.dp))

        editingGround?.let { ground ->
            GroundEditForm(
                ground = ground,
                onSave = {
                    onSaveGround(it)
                    editingGround = null
                },
                onCancel = {
                    editingGround = null
                }
            )
        }
    }
}

// ---------------- GROUND CARD ----------------
@Composable
fun GroundCard(
    ground: FutsalGround,
    onEdit: (FutsalGround) -> Unit
) {
    Surface(
        color = MidnightBlue_Admin,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, Color.Gray),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            // Ground Image
            Image(
                painter = painterResource(id = ground.imageRes),
                contentDescription = ground.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = ground.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkCard
                )
                Text("Location: ${ground.location}", color = DarkCard)
                Text("Rating: ${ground.rating}", color = DarkCard)
                Text("Price: Rs ${ground.price}", color = DarkCard)

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { onEdit(ground) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Edit")
                }
            }
        }
    }
}
// ---------------- EDIT FORM ----------------
@Composable
fun GroundEditForm(
    ground: FutsalGround,
    onSave: (FutsalGround) -> Unit,
    onCancel: () -> Unit
) {

    var name by remember { mutableStateOf(ground.name) }
    var location by remember { mutableStateOf(ground.location) }
    var distance by remember { mutableStateOf(ground.distance) }
    var rating by remember { mutableStateOf(ground.rating.toString()) }
    var reviewCount by remember { mutableStateOf(ground.reviewCount.toString()) }
    var price by remember { mutableStateOf(ground.price.toString()) }
    var facilities by remember { mutableStateOf(ground.facilities.joinToString(",")) }
    var description by remember { mutableStateOf(ground.description) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(DarkCard, RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {

        Text("Edit Ground", fontSize = 20.sp)

        Spacer(modifier = Modifier.height(12.dp))

        AdminField("Name", name) { name = it }
        AdminField("Location", location) { location = it }
        AdminField("Distance", distance) { distance = it }
        AdminField("Rating", rating) { rating = it }
        AdminField("Review Count", reviewCount) { reviewCount = it }
        AdminField("Price", price) { price = it }
        AdminField("Facilities (comma separated)", facilities) { facilities = it }
        AdminField("Description", description) { description = it }

        Spacer(modifier = Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {

            Button(
                onClick = {
                    val updated = ground.copy(
                        name = name,
                        location = location,
                        distance = distance,
                        rating = rating.toDoubleOrNull() ?: 0.0,
                        reviewCount = reviewCount.toIntOrNull() ?: 0,
                        price = price.toIntOrNull() ?: 0,
                        facilities = facilities.split(",").map { it.trim() },
                        description = description
                    )
                    onSave(updated)
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Save")
            }

            Button(
                onClick = onCancel,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text("Cancel")
            }
        }
    }
}

// ---------------- REUSABLE FIELD ----------------
@Composable
fun AdminField(
    label: String,
    value: String,
    onChange: (String) -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 6.dp)) {

        Text(label, fontSize = 12.sp)

        OutlinedTextField(
            value = value,
            onValueChange = onChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
    }
}
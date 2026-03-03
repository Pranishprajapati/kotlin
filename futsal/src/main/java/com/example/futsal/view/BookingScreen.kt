package com.example.futsal.view

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.futsal.model.BookingModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.foundation.lazy.items
import com.example.futsal.repository.UserRepoImpl
import com.example.futsal.viewmodel.UserViewModel

val DeepBlack_B = Color(0xFF0B0E11)
val MidnightBlue_B = Color(0xFF161B22)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingScreen(groundName: String, price: Int, onBack: () -> Unit) {
    val user = FirebaseAuth.getInstance().currentUser
    val context = LocalContext.current
    val currentUser = FirebaseAuth.getInstance().currentUser
    val dbRef = FirebaseDatabase.getInstance().reference.child("bookings") // Realtime DB reference
    var selectedDateIdx by remember { mutableIntStateOf(0) }
    var selectedSlot by remember { mutableStateOf<String?>(null) }
    val userViewModel = remember { UserViewModel(UserRepoImpl()) }
    val days = remember { getNextSevenDays() }
    val timeSlots = listOf("06:00 AM","07:00 AM","08:00 AM","09:00 AM","10:00 AM","11:00 AM",
        "12:00 PM","01:00 PM","02:00 PM","03:00 PM","04:00 PM","05:00 PM","06:00 PM","07:00 PM",
        "08:00 PM","09:00 PM","10:00 PM")

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = { Text("Select Slot", color = Color.White, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize().padding(horizontal = 24.dp)) {
            Text(text = groundName, color = NeonBlue_Main, fontSize = 24.sp, fontWeight = FontWeight.Black)
            Text(text = "NPR $price / per hour", color = Color.White.copy(alpha = 0.6f), fontSize = 14.sp)
            Spacer(modifier = Modifier.height(32.dp))

            // Date selection
            Text("Choose Date", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(16.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(days.size) { index ->
                    val day = days[index]
                    Surface(
                        modifier = Modifier.width(70.dp).height(90.dp).clickable { selectedDateIdx = index },
                        shape = RoundedCornerShape(16.dp),
                        color = if (selectedDateIdx == index) NeonBlue_Main else GlassWhite_Main,
                        border = if (selectedDateIdx == index) null else BorderStroke(1.dp, GlassBorder_Main)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                            Text(day.first, color = if (selectedDateIdx == index) DeepBlack_B else Color.White.copy(alpha = 0.6f), fontSize = 12.sp)
                            Text(day.second, color = if (selectedDateIdx == index) DeepBlack_B else Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
            Text("Available Slots", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(16.dp))
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(timeSlots) { slot ->
                    Surface(
                        modifier = Modifier.fillMaxWidth().height(50.dp).clickable { selectedSlot = slot },
                        shape = RoundedCornerShape(12.dp),
                        color = if (selectedSlot == slot) NeonBlue_Main else GlassWhite_Main,
                        border = if (selectedSlot == slot) null else BorderStroke(1.dp, GlassBorder_Main)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(text = slot, color = if (selectedSlot == slot) DeepBlack_B else Color.White, fontSize = 12.sp, fontWeight = if (selectedSlot == slot) FontWeight.Bold else FontWeight.Normal)
                        }
                    }
                }
            }

            Surface(color = GlassWhite_Main, shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp), border = BorderStroke(1.dp, GlassBorder_Main), modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Total Amount", color = Color.White.copy(alpha = 0.6f))
                        Text("NPR $price", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            if (selectedSlot == null) {
                                Toast.makeText(context, "Please select a time slot", Toast.LENGTH_SHORT).show()
                                return@Button
                            }

                            if (user == null) {
                                Toast.makeText(context, "User not logged in", Toast.LENGTH_SHORT).show()
                                return@Button
                            }

                            val day = days[selectedDateIdx]

                            val booking = BookingModel(
                                userId = user.uid,
                                groundName = groundName,
                                date = "${day.first}, ${day.second}",
                                timeSlot = selectedSlot!!,
                                price = price
                            )

                            userViewModel.bookGround(booking) { success: Boolean, msg: String ->
                                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                                if (success) onBack()
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = NeonBlue_Main)
                    ) {
                        Text("CONFIRM BOOKING", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

fun getNextSevenDays(): List<Pair<String, String>> {
    val days = mutableListOf<Pair<String, String>>()
    val sdfDay = SimpleDateFormat("EEE", Locale.getDefault())
    val sdfDate = SimpleDateFormat("dd", Locale.getDefault())
    val cal = Calendar.getInstance()
    for (idx in 0..6) {
        days.add(sdfDay.format(cal.time) to sdfDate.format(cal.time))
        cal.add(Calendar.DATE, 1)
    }
    return days
}

@Composable
fun BookingsTab() {

    val context = LocalContext.current
    val currentUser = FirebaseAuth.getInstance().currentUser
    val dbRef = FirebaseDatabase.getInstance().reference.child("bookings")

    var bookings by remember { mutableStateOf<List<BookingModel>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    // Proper Firebase listener lifecycle
    DisposableEffect(currentUser) {

        if (currentUser == null) {
            isLoading = false
            onDispose { }
        } else {

            val query = dbRef
                .orderByChild("userId")
                .equalTo(currentUser.uid)

            val listener = object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = mutableListOf<BookingModel>()

                    snapshot.children.forEach { child ->
                        val booking = child.getValue(BookingModel::class.java)
                        if (booking != null) {
                            list.add(booking)
                        }
                    }

                    bookings = list
                    isLoading = false
                }

                override fun onCancelled(error: DatabaseError) {
                    isLoading = false
                    Toast.makeText(
                        context,
                        "Error: ${error.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            query.addValueEventListener(listener)

            onDispose {
                query.removeEventListener(listener)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "My Bookings",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Black
        )

        Spacer(modifier = Modifier.height(32.dp))

        when {
            isLoading -> {
                CircularProgressIndicator(color = NeonBlue_Main)
            }

            bookings.isEmpty() -> {
                Surface(
                    color = GlassWhite_Main,
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.History,
                            contentDescription = null,
                            tint = Color.White.copy(alpha = 0.2f),
                            modifier = Modifier.size(64.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            "No active bookings yet",
                            color = Color.White.copy(alpha = 0.5f)
                        )
                    }
                }
            }

            else -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(bookings) { booking ->

                        Surface(
                            color = GlassWhite_Main,
                            shape = RoundedCornerShape(24.dp),
                            border = BorderStroke(1.dp, GlassBorder_Main),
                            modifier = Modifier.fillMaxWidth()
                        ) {

                            Column(modifier = Modifier.padding(20.dp)) {

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {

                                    Column {
                                        Text(
                                            booking.groundName,
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 18.sp
                                        )

                                        Text(
                                            booking.date,
                                            color = Color.White.copy(alpha = 0.6f),
                                            fontSize = 14.sp
                                        )
                                    }

                                    Surface(
                                        color = NeonBlue_Main.copy(alpha = 0.1f),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text(
                                            "Upcoming",
                                            color = NeonBlue_Main,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(
                                                horizontal = 8.dp,
                                                vertical = 4.dp
                                            )
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Default.AccessTime,
                                        contentDescription = null,
                                        tint = NeonBlue_Main,
                                        modifier = Modifier.size(16.dp)
                                    )

                                    Spacer(modifier = Modifier.width(8.dp))

                                    Text(
                                        booking.timeSlot,
                                        color = Color.White,
                                        fontSize = 14.sp
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    "Price Paid: NPR ${booking.price}",
                                    color = Color.White.copy(alpha = 0.5f),
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
package com.example.futsal.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.futsal.R
import com.example.futsal.model.BookingModel
import com.example.futsal.model.FutsalGround
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class AdminViewModel(
    private val skipFirebase: Boolean = false // Use true for unit tests
) : ViewModel() {

    private val db = if (!skipFirebase) FirebaseDatabase.getInstance() else null

    // ---------------- BOOKINGS ----------------
    fun getAllBookings(callback: (List<BookingModel>) -> Unit) {
        if (skipFirebase) return // Skip Firebase when testing

        db?.getReference("bookings")
            ?.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = mutableListOf<BookingModel>()
                    snapshot.children.forEach { child ->
                        child.getValue(BookingModel::class.java)?.let {
                            list.add(it.copy(id = child.key ?: ""))
                        }
                    }
                    callback(list)
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }

    fun updateBookingStatus(bookingId: String, status: String) {
        if (skipFirebase) return
        db?.getReference("bookings")
            ?.child(bookingId)
            ?.child("status")
            ?.setValue(status)
    }

    // ---------------- GROUNDS STATE ----------------
    var grounds by mutableStateOf(
        listOf(
            FutsalGround(
                id = "futsal-arena-boudha",
                name = "Futsal Arena Boudha",
                location = "Kathmandu",
                distance = "4.2 km away",
                rating = 4.8,
                reviewCount = 156,
                price = 800,
                facilities = listOf("Artificial Turf", "Floodlights", "Seating", "Refreshments"),
                imageRes = R.drawable.boudha,
                description = "Futsal Arena Boudha is a top-class indoor futsal facility located in the heart of Kathmandu."
            ),
            FutsalGround(
                id = "dreamers-futsal",
                name = "Dreamers Futsal",
                location = "Lalitpur",
                distance = "2.5 km away",
                rating = 4.5,
                reviewCount = 92,
                price = 700,
                facilities = listOf("Artificial Turf", "Parking", "Canteen"),
                imageRes = R.drawable.kumari,
                description = "Dreamers Futsal offers a modern artificial turf field with full amenities including parking and a canteen."
            ),
            FutsalGround(
                id = "bhaktapur-futsal",
                name = "Bhaktapur Futsal",
                location = "Sallaghari, Bhaktapur",
                distance = "8.1 km away",
                rating = 4.7,
                reviewCount = 110,
                price = 750,
                facilities = listOf("Turf Surface", "Floodlights", "Canteen"),
                imageRes = R.drawable.bhaktapur,
                description = "Bhaktapur Futsal offers a well-maintained turf surface with excellent floodlights, perfect for evening matches."
            ),
            FutsalGround(
                id = "field-futsal",
                name = "Field Futsal",
                location = "Sanepa Road, Lalitpur",
                distance = "1.8 km away",
                rating = 4.6,
                reviewCount = 74,
                price = 650,
                facilities = listOf("Indoor", "Artificial Turf", "Floodlights", "Parking"),
                imageRes = R.drawable.field,
                description = "Field Futsal provides a comfortable indoor arena with high-quality artificial turf. Equipped with floodlights and ample parking."
            ),
            FutsalGround(
                id = "sankhamul-futsal",
                name = "Sankhamul Futsal",
                location = "Kathmandu",
                distance = "3.2 km away",
                rating = 4.4,
                reviewCount = 65,
                price = 700,
                facilities = listOf("Outdoor", "Floodlights", "Canteen"),
                imageRes = R.drawable.sankhamul,
                description = "Sankhamul Futsal is an outdoor ground offering a lively atmosphere with top-notch floodlights."
            ),
            FutsalGround(
                id = "gokarna-futsal",
                name = "Gokarna Futsal",
                location = "Kathmandu",
                distance = "10.5 km away",
                rating = 4.7,
                reviewCount = 42,
                price = 850,
                facilities = listOf("Indoor", "Premium Turf", "Locker Rooms"),
                imageRes = R.drawable.gokarna,
                description = "Gokarna Futsal features a premium indoor turf and modern locker rooms. Perfect for training sessions and professional games."
            ),
            FutsalGround(
                id = "kumari-futsal",
                name = "Kumari Futsal and Snooker",
                location = "Paknajol, Kathmandu",
                distance = "1.2 km away",
                rating = 4.6,
                reviewCount = 128,
                price = 750,
                facilities = listOf("Indoor/Outdoor", "Artificial Turf", "Recreation Center"),
                imageRes = R.drawable.kumari,
                description = "Kumari Futsal and Snooker combines indoor and outdoor futsal facilities with a recreation center."
            )

        )
    )
        private set

    // ---------------- CRUD FOR GROUNDS ----------------
    fun deleteGroundById(id: String) {
        grounds = grounds.filter { it.id != id }
    }

    fun addOrUpdateGround(updated: FutsalGround) {
        grounds = if (updated.id.isBlank()) {
            // ADD NEW
            grounds + updated.copy(id = System.currentTimeMillis().toString())
        } else {
            // UPDATE EXISTING
            grounds.map { if (it.id == updated.id) updated else it }
        }
    }
}
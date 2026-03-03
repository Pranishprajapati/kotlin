package com.example.futsal.model
data class BookingModel(
    val id: String = "",
    val userId: String = "",
    val groundName: String = "",
    val date: String = "",
    val timeSlot: String = "",
    val price: Int = 0,
    val status: String = "Pending"
)
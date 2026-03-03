package com.example.futsal.model

data class TimeSlotModel(
    val id: String = "",
    val startTime: String = "",
    val endTime: String = "",
    val bookedBy: String? = null
)
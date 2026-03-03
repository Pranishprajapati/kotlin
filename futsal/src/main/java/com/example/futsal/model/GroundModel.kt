package com.example.futsal.model

data class FutsalGround(
    val id: String,
    val name: String,
    val location: String,
    val distance: String,
    val rating: Double,
    val reviewCount: Int,
    val price: Int,
    val facilities: List<String>,
    val imageRes: Int,
    val description: String
)

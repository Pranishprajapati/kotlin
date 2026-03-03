package com.example.futsal.model

data class UserModel(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val city: String = "",
    val address: String = "",
    val gender: String = "",
    val imageUrl: String = "",
    val role: String = "user"
)

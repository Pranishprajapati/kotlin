package com.example.pranish.model

data class UserModel(
    val userId: String="",
    val email: String="",
    val password: String="",
    val firstName: String="",
    val lastName: String="",
    val dob: String="",

){
    fun toMap() : Map<String,Any?>{
        return mapOf(
            "userId" to userId,
            "email" to email,
            "password" to password,
            "firstName" to firstName,
            "lastName" to lastName,
            "dob" to dob,
            )
    }
}
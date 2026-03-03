// UserRepo.kt
package com.example.futsal.repository

import android.content.Context
import android.net.Uri
import com.example.futsal.model.UserModel
import com.example.futsal.model.BookingModel

interface UserRepo {
    fun login(email: String, password: String, callback: (success: Boolean, message: String, isAdmin: Boolean) -> Unit)
    fun register(user: UserModel, password: String, callback: (Boolean, String) -> Unit)
    fun forgotPassword(email: String, callback: (Boolean, String) -> Unit)
    fun uploadImage(context: Context, imageUri: Uri, callback: (String?) -> Unit)
    fun getUserData(uid: String, callback: (UserModel?) -> Unit)
    fun updateUserData(user: UserModel, callback: (Boolean, String) -> Unit)
    fun bookGround(booking: BookingModel, callback: (Boolean, String) -> Unit)
    fun getUserBookings(userId: String, callback: (List<BookingModel>?) -> Unit)
}
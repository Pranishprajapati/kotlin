package com.example.futsal.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.futsal.model.BookingModel
import com.example.futsal.model.UserModel
import com.example.futsal.repository.UserRepo

class UserViewModel(private val repo: UserRepo) : ViewModel() {

    fun login(email: String, password: String, callback: (success: Boolean, message: String, isAdmin: Boolean) -> Unit) {
        repo.login(email, password, callback)
    }

    fun register(user: UserModel, password: String, callback: (Boolean, String) -> Unit) {
        repo.register(user, password, callback)
    }

    fun forgotPassword(email: String, callback: (Boolean, String) -> Unit) {
        repo.forgotPassword(email, callback)
    }

    fun uploadImage(context: Context, imageUri: Uri, callback: (String?) -> Unit) {
        repo.uploadImage(context, imageUri, callback)
    }

    fun getUserData(uid: String, callback: (UserModel?) -> Unit) {
        repo.getUserData(uid, callback)
    }

    fun updateUserData(user: UserModel, callback: (Boolean, String) -> Unit) {
        repo.updateUserData(user, callback)
    }

    fun bookGround(booking: BookingModel, callback: (Boolean, String) -> Unit) {
        repo.bookGround(booking, callback)
    }

    fun getUserBookings(userId: String, callback: (List<BookingModel>?) -> Unit) {
        repo.getUserBookings(userId, callback)
    }
}
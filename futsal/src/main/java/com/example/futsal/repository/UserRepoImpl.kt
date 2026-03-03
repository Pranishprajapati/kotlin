package com.example.futsal.repository

import android.content.Context
import android.net.Uri
import com.example.futsal.model.UserModel
import com.example.futsal.model.BookingModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class UserRepoImpl : UserRepo {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseDatabase.getInstance().reference

    override fun login(email: String, password: String, callback: (Boolean, String, Boolean) -> Unit) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val uid = auth.currentUser?.uid ?: ""
                db.child("users").child(uid).get().addOnSuccessListener { snapshot ->
                    val isAdmin = snapshot.child("isAdmin").getValue(Boolean::class.java) ?: false
                    callback(true, "Login Successful", isAdmin)
                }.addOnFailureListener {
                    callback(true, "Login Successful", false)
                }
            } else {
                callback(false, task.exception?.message ?: "Login Failed", false)
            }
        }
    }

    override fun register(user: UserModel, password: String, callback: (Boolean, String) -> Unit) {
        auth.createUserWithEmailAndPassword(user.email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val uid = auth.currentUser?.uid ?: ""
                val userMap = mapOf(
                    "uid" to uid,
                    "name" to user.name,
                    "email" to user.email,
                    "phoneNumber" to user.phoneNumber,
                    "city" to user.city,
                    "address" to user.address,
                    "gender" to user.gender,
                    "isAdmin" to false
                )
                db.child("users").child(uid).setValue(userMap).addOnSuccessListener {
                    callback(true, "Registration Successful")
                }.addOnFailureListener {
                    callback(false, it.message ?: "Registration Failed")
                }
            } else {
                callback(false, task.exception?.message ?: "Registration Failed")
            }
        }
    }

    override fun forgotPassword(email: String, callback: (Boolean, String) -> Unit) {
        auth.sendPasswordResetEmail(email)
            .addOnSuccessListener { callback(true, "Reset link sent to email") }
            .addOnFailureListener { callback(false, it.message ?: "Failed to send reset email") }
    }

    override fun uploadImage(context: Context, imageUri: Uri, callback: (String?) -> Unit) {
        // Implement FirebaseStorage upload if needed
        callback(null)
    }

    override fun getUserData(uid: String, callback: (UserModel?) -> Unit) {
        db.child("users").child(uid).get().addOnSuccessListener { snapshot ->
            snapshot.getValue(UserModel::class.java)?.let { callback(it) } ?: callback(null)
        }.addOnFailureListener { callback(null) }
    }

    override fun updateUserData(user: UserModel, callback: (Boolean, String) -> Unit) {
        db.child("users").child(user.uid).setValue(user).addOnSuccessListener {
            callback(true, "Profile Updated")
        }.addOnFailureListener {
            callback(false, it.message ?: "Update Failed")
        }
    }

    override fun bookGround(
        booking: BookingModel,
        callback: (Boolean, String) -> Unit
    ) {
        val currentUser = auth.currentUser

        if (currentUser == null) {
            callback(false, "User not logged in")
            return
        }

        val bookingId = db.child("bookings").push().key

        if (bookingId == null) {
            callback(false, "Failed to generate booking ID")
            return
        }

        val bookingWithUid = booking.copy(userId = currentUser.uid)

        db.child("bookings")
            .child(bookingId)
            .setValue(bookingWithUid)
            .addOnSuccessListener {
                callback(true, "Booking successful")
            }
            .addOnFailureListener {
                callback(false, it.message ?: "Booking failed")
            }
    }


    override fun getUserBookings(userId: String, callback: (List<BookingModel>?) -> Unit) {
        db.child("bookings").orderByChild("userId").equalTo(userId).get().addOnSuccessListener { snapshot ->
            val list = snapshot.children.mapNotNull { it.getValue(BookingModel::class.java) }
            callback(list)
        }.addOnFailureListener { callback(null) }
    }
}
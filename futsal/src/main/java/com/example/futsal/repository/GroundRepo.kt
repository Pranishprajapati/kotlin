package com.example.futsal.repository

import com.example.futsal.model.TimeSlotModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class GroundRepo {

    private val db = FirebaseFirestore.getInstance()

    suspend fun getTimeSlots(groundId: String): List<TimeSlotModel> {
        val snapshot = db.collection("grounds")
            .document(groundId)
            .collection("slots")
            .get()
            .await()

        return snapshot.toObjects(TimeSlotModel::class.java)
    }

    suspend fun bookSlot(groundId: String, slotId: String, userId: String) {
        db.collection("grounds")
            .document(groundId)
            .collection("slots")
            .document(slotId)
            .update("bookedBy", userId)
    }
}
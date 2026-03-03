package com.example.futsal.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.futsal.model.TimeSlotModel
import com.example.futsal.repository.GroundRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BookingViewModel : ViewModel() {

    private val repo = GroundRepo()

    private val _slots = MutableStateFlow<List<TimeSlotModel>>(emptyList())
    val slots: StateFlow<List<TimeSlotModel>> = _slots

    fun loadSlots(groundId: String) {
        viewModelScope.launch {
            _slots.value = repo.getTimeSlots(groundId)
        }
    }

    fun bookSlot(groundId: String, slotId: String, userId: String) {
        viewModelScope.launch {
            repo.bookSlot(groundId, slotId, userId)
            loadSlots(groundId)
        }
    }
}
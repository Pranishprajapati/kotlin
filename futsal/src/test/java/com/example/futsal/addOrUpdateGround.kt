package com.example.futsal

import com.example.futsal.model.FutsalGround
import com.example.futsal.viewmodel.AdminViewModel
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class AdminViewModelGroundTest {

    private lateinit var viewModel: AdminViewModel

    @Before
    fun setup() {
        // skipFirebase = true ensures no Firebase dependency
        viewModel = AdminViewModel(skipFirebase = true)
    }

    @Test
    fun addGround_shouldIncreaseGroundList() {
        val initialSize = viewModel.grounds.size

        val newGround = FutsalGround(
            id = "", // blank id → will be generated
            name = "Test Futsal Ground",
            location = "Test Location",
            distance = "0.5 km away",
            rating = 4.0,
            reviewCount = 10,
            price = 500,
            facilities = listOf("Indoor", "Lighting"),
            imageRes = 0,
            description = "A test futsal ground"
        )

        viewModel.addOrUpdateGround(newGround)

        // Verify size increased by 1
        assertEquals(initialSize + 1, viewModel.grounds.size)

        // Verify the new ground exists in the list
        val addedGround = viewModel.grounds.last()
        assertEquals("Test Futsal Ground", addedGround.name)
        assertTrue(addedGround.id.isNotBlank())
    }

    @Test
    fun updateGround_shouldModifyExistingGround() {
        val existingGround = viewModel.grounds.first()
        val updatedGround = existingGround.copy(name = "Updated Name")

        viewModel.addOrUpdateGround(updatedGround)

        // Verify the ground is updated
        val groundAfterUpdate = viewModel.grounds.find { it.id == existingGround.id }
        assertNotNull(groundAfterUpdate)
        assertEquals("Updated Name", groundAfterUpdate?.name)
    }

    @Test
    fun deleteGroundById_shouldRemoveGround() {
        val groundToDelete = viewModel.grounds.first()
        val initialSize = viewModel.grounds.size

        viewModel.deleteGroundById(groundToDelete.id)

        // Verify size decreased by 1
        assertEquals(initialSize - 1, viewModel.grounds.size)

        // Verify the ground is gone
        assertFalse(viewModel.grounds.any { it.id == groundToDelete.id })
    }
}
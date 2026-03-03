package com.example.futsal

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.futsal.model.FutsalGround
import com.example.futsal.viewmodel.AdminViewModel
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AdminViewModelGroundInstrumentedTest {

    private lateinit var viewModel: AdminViewModel

    @Before
    fun setup() {
        // In instrumented test, skipFirebase is also true to avoid Firebase
        viewModel = AdminViewModel(skipFirebase = true)
    }

    @Test
    fun addGround_shouldIncreaseGroundList() {
        val initialSize = viewModel.grounds.size

        val newGround = FutsalGround(
            id = "",
            name = "Instrumented Test Ground",
            location = "Instrument Location",
            distance = "1.0 km away",
            rating = 4.2,
            reviewCount = 20,
            price = 600,
            facilities = listOf("Indoor", "Parking"),
            imageRes = 0,
            description = "Instrumented test futsal ground"
        )

        viewModel.addOrUpdateGround(newGround)

        assertEquals(initialSize + 1, viewModel.grounds.size)
        val addedGround = viewModel.grounds.last()
        assertEquals("Instrumented Test Ground", addedGround.name)
        assertTrue(addedGround.id.isNotBlank())
    }

    @Test
    fun updateGround_shouldModifyExistingGround() {
        val existingGround = viewModel.grounds.first()
        val updatedGround = existingGround.copy(name = "Updated Instrumented Name")

        viewModel.addOrUpdateGround(updatedGround)

        val groundAfterUpdate = viewModel.grounds.find { it.id == existingGround.id }
        assertNotNull(groundAfterUpdate)
        assertEquals("Updated Instrumented Name", groundAfterUpdate?.name)
    }

    @Test
    fun deleteGroundById_shouldRemoveGround() {
        val groundToDelete = viewModel.grounds.first()
        val initialSize = viewModel.grounds.size

        viewModel.deleteGroundById(groundToDelete.id)

        assertEquals(initialSize - 1, viewModel.grounds.size)
        assertFalse(viewModel.grounds.any { it.id == groundToDelete.id })
    }

    @Test
    fun useAppContext() {
        // Context test just to show instrumentation works
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.futsal", appContext.packageName)
    }
}
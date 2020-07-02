package xyz.goshanchik.rainet.planttracker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import xyz.goshanchik.rainet.model.PlantDatabaseDao
import java.lang.IllegalArgumentException

class PlantTrackerViewModelFactory(
    private val dataSource: PlantDatabaseDao
): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(PlantTrackerViewModel::class.java))
            return PlantTrackerViewModel(dataSource) as T
        throw IllegalArgumentException("no ViewModel class found.")
    }
}
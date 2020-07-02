package xyz.goshanchik.rainet.plantdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import xyz.goshanchik.rainet.model.PlantDatabaseDao
import java.lang.IllegalArgumentException

class PlantDetailViewModelFactory(
    private val key: Long, private val dataSource: PlantDatabaseDao): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(PlantDetailViewModel::class.java))
            return PlantDetailViewModel(key, dataSource) as T
        throw IllegalArgumentException("no ViewModel class found.")
    }
}
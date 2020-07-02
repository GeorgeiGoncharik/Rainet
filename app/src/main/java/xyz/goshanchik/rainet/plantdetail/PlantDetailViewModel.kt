package xyz.goshanchik.rainet.plantdetail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import xyz.goshanchik.rainet.model.Plant
import xyz.goshanchik.rainet.model.PlantDatabaseDao
import java.util.*

fun <T> MutableLiveData<T>.forceRefresh() {
    Log.i("PlantDetailViewModel" , "forceRefresh called.")
    this.value = this.value
}

class PlantDetailViewModel(private val key: Long, private val dataSource: PlantDatabaseDao) : ViewModel() {

    private val viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val plant: MutableLiveData<Plant?> = MutableLiveData<Plant?>()

    private val _navigateToTakeImage = MutableLiveData<Boolean>(false)
    val navigateToTakeImage: LiveData<Boolean>
        get() = _navigateToTakeImage

    fun navigateToTakeImage(){
        _navigateToTakeImage.value = true
    }

    fun onNavigateToTakeImage(){
        _navigateToTakeImage.value = false
    }

    init {
        uiScope.launch {
            withContext(Dispatchers.IO){
                plant.postValue(dataSource.get(key))
            }
        }
    }

    fun changePhotoUri(uri: String){
        Log.i("PlantDetailViewModel" , "changePhotoUri called.")
        plant.value?.photoUri = uri
        plant.forceRefresh()
        updatePlant()
    }

    fun changeName(name: String){
        plant.value?.name = name
        updatePlant()
    }

    fun changeDescription(description: String){
        plant.value?.description = description
        updatePlant()
    }

    fun incPeriod(){
        plant.value?.wateringPeriod?.plus(1)
        updatePlant()
    }

    fun decPeriod(){
        plant.value?.wateringPeriod?.minus(1)
        updatePlant()
    }

    fun onWatered() {
        plant.value?.recentWatering = Calendar.getInstance()
        updatePlant()
   }

    private fun updatePlant() {
        Log.i("PlantDetailViewModel" , "updatePlant called.")
        uiScope.launch {
            withContext(Dispatchers.IO){
                plant.value?.let { dataSource.update(plant = it) }
            }
        }
    }
}
package xyz.goshanchik.rainet.planttracker

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import xyz.goshanchik.rainet.model.Plant
import xyz.goshanchik.rainet.model.PlantDatabaseDao

class PlantTrackerViewModel(private val dataSource: PlantDatabaseDao) : ViewModel() {

    private val viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val plants: LiveData<List<Plant>> = dataSource.getAll()

    private val _navigateToPlantDetailFragment = MutableLiveData<Plant?>()
    val navigateToPlantDetailFragment: LiveData<Plant?>
        get() = _navigateToPlantDetailFragment

    fun navigateToPlantDetailFragment(plant: Plant){
        _navigateToPlantDetailFragment.value =  plant
    }

    fun doneNavigating(){
        _navigateToPlantDetailFragment.value = null
    }

    fun add(item: Plant = Plant()){
        Log.i("PlantTrackerViewModel", "added new plant: ${item.id}")
        uiScope.launch {
            withContext(Dispatchers.IO){
                dataSource.insert(item)
            }
        }
    }

    fun addAndNavigate(item: Plant = Plant()){
        add(item)
         uiScope.launch {
             withContext(Dispatchers.IO){
                 _navigateToPlantDetailFragment.postValue(dataSource.getRecent())
             }
         }
    }

    fun delete(item: Plant){
        Log.i("PlantTrackerViewModel", "deleted plant, id ${item.id}")
        uiScope.launch {
            withContext(Dispatchers.IO){
                dataSource.delete(item)
            }
        }
    }

    fun onSwiped(item: Plant){

        var currentTime = 0L

        val timer = object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND) {

            override fun onTick(millisUntilFinished: Long) {
                currentTime = (millisUntilFinished / ONE_SECOND)
            }

            override fun onFinish(){

            }
        }.start()
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    companion object {
        // These represent different important times
        // This is when the game is over
        const val DONE = 0L
        // This is the number of milliseconds in a second
        const val ONE_SECOND = 1000L
        // This is the total time of the game
        const val COUNTDOWN_TIME = 5000L
    }
}
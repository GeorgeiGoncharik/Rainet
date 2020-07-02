package xyz.goshanchik.rainet.model

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PlantDatabaseDao {

    @Insert
    fun insert(plant: Plant)

    @Delete
    fun delete(plant: Plant)

    @Update
    fun update(plant: Plant)

    @Query("SELECT * FROM plant_table")
    fun getAll(): LiveData<List<Plant>>

    @Query("SELECT * FROM plant_table WHERE id = :key")
    fun get(key: Long): Plant?

    @Query("SELECT * FROM plant_table ORDER BY id DESC LIMIT 1")
    fun getRecent(): Plant?
}
package xyz.goshanchik.rainet.model

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.temporal.ChronoUnit
import java.util.*

@Entity(tableName = "plant_table")
data class Plant(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "name")
    var name: String = "Untitled",

    @ColumnInfo(name = "description")
    var description: String = "",

    @ColumnInfo(name = "photo_uri")
    var photoUri: String = "",

    @ColumnInfo(name = "watering_period")
    var wateringPeriod: Int = 1,

    @ColumnInfo(name = "recent_watering")
    var recentWatering: Calendar = Calendar.getInstance()
)

{
    @RequiresApi(Build.VERSION_CODES.O)
    fun daysTillWatering(): Long {
        val daysBetween = ChronoUnit.DAYS.between(
            recentWatering.toInstant(),
            Calendar.getInstance().toInstant())

        return wateringPeriod - daysBetween
    }
}



package xyz.goshanchik.rainet.model

import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import androidx.room.Database
import androidx.room.TypeConverters

@TypeConverters(Converters::class)
@Database(entities = [Plant::class], version = 1, exportSchema = false)
abstract class PlantDatabase: RoomDatabase() {

    abstract val databaseDao: PlantDatabaseDao

    companion object{
        @Volatile
        private var INSTANCE: PlantDatabase? = null

        fun getInstance(context: Context): PlantDatabase{
            synchronized(this){
                var instance = INSTANCE

                if(instance == null){
                    instance =  Room.databaseBuilder(
                        context.applicationContext,
                        PlantDatabase::class.java,
                        "rainet_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()

                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}
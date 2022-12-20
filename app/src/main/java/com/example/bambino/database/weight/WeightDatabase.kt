package com.example.bambino.database.weight

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Weight::class], version = 3, exportSchema = false)
abstract class WeightDatabase : RoomDatabase() {

    abstract val weightDatabaseDao: WeightDatabaseDao

    companion object {
        @Volatile
        private var INSTANCE: WeightDatabase? = null

        fun getInstance(context: Context): WeightDatabase {
            Log.i("MemoriesDatabase", "CREATING INSTANCE OF ROOM DB $INSTANCE")
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        WeightDatabase::class.java,
                        "weight_database"
                    ).fallbackToDestructiveMigration().build()
                    INSTANCE = instance
                }
                return instance
            }
        }

    }

}
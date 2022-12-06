package com.example.bambino.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [TrackedAction::class], version = 1, exportSchema = false)
abstract class ActionsDatabase : RoomDatabase() {

    abstract val actionsDatabaseDao: ActionsDatabaseDao

    companion object {
        @Volatile
        private var INSTANCE: ActionsDatabase? = null

        fun getInstance(context: Context): ActionsDatabase {
            Log.i("ActionsDatabase", "CREATING INSTANCE OF ROOM DB $INSTANCE")
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        ActionsDatabase::class.java,
                        "tracked_actions_database"
                    ).fallbackToDestructiveMigration().build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }

}
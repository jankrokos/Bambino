package com.example.bambino.database

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ActionsDatabaseDao {

    @Insert
    fun insert(action: TrackedAction){
        Log.i("ActionsDatabaseDao", "insert db called")
    }

    @Update
    fun update(action: TrackedAction)

    @Query("SELECT * FROM daily_tracked_actions_table WHERE actionId = :key")
    fun get(key: Long): TrackedAction

    @Query("DELETE FROM daily_tracked_actions_table")
    fun clear(){
        Log.i("ActionsDatabaseDao", "clear db called")
    }

    @Query("SELECT * FROM daily_tracked_actions_table ORDER BY actionId DESC")
    fun getAllActions(): LiveData<List<TrackedAction>>

    @Query("SELECT * FROM daily_tracked_actions_table WHERE action_time = :day ORDER BY actionId DESC")
    fun getTodayActions(day: Long): LiveData<List<TrackedAction>>

    @Query("SELECT * FROM daily_tracked_actions_table ORDER BY actionId DESC LIMIT 1")
    fun getNewAction(): TrackedAction?
}
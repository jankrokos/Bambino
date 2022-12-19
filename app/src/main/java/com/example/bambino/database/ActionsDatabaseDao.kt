package com.example.bambino.database

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ActionsDatabaseDao {

    @Insert
    suspend fun insert(action: TrackedAction)

    @Update
    suspend fun update(action: TrackedAction)

    @Query("SELECT * FROM daily_tracked_actions_table WHERE actionId = :key")
    suspend fun get(key: Long): TrackedAction

    @Query("DELETE FROM daily_tracked_actions_table")
    suspend fun clear()

    @Query("DELETE FROM daily_tracked_actions_table WHERE actionId = :key")
    suspend fun deleteWithId(key: Long)

    @Query("SELECT * FROM daily_tracked_actions_table ORDER BY action_time ASC")
    fun getAllActions(): LiveData<List<TrackedAction>>

    @Query("SELECT * FROM daily_tracked_actions_table WHERE action_time > :dayStart AND action_time < :dayEnd ORDER BY action_time")
    fun getTodayActions(dayStart: Long, dayEnd: Long): LiveData<List<TrackedAction>>

    @Query("DELETE FROM daily_tracked_actions_table WHERE action_time > :dayStart AND action_time < :dayEnd")
    suspend fun clearDay(dayStart: Long, dayEnd: Long)

    @Query("SELECT * FROM daily_tracked_actions_table WHERE actionId > :id1 AND actionId < :id2")
    fun getTest(id1: Long, id2: Long): LiveData<List<TrackedAction>>

    @Query("SELECT * FROM daily_tracked_actions_table ORDER BY actionId DESC LIMIT 1")
    suspend fun getNewAction(): TrackedAction
}
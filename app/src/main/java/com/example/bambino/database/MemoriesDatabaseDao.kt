package com.example.bambino.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface MemoriesDatabaseDao {

    @Insert
    suspend fun insert(action: Memory)

    @Query("SELECT * FROM memories_table WHERE memoryId = :key")
    suspend fun get(key: Long): Memory

    @Query("DELETE FROM memories_table")
    suspend fun clear()

    @Query("SELECT * FROM memories_table ORDER BY memory_date ASC")
    fun getAllActions(): LiveData<List<Memory>>
}
package com.example.bambino.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface MemoriesDatabaseDao {

    @Insert
    suspend fun insert(memory: Memory)

    @Update
    suspend fun update(memory: Memory)

    @Query("SELECT * FROM memories_table WHERE memoryId = :key")
    suspend fun getMemoryWithId(key: Long): Memory

    @Query("DELETE FROM memories_table")
    suspend fun clear()

    @Query("DELETE FROM memories_table WHERE memoryId = :key")
    suspend fun delete(key: Long)

    @Query("SELECT * FROM memories_table ORDER BY memory_date ASC")
    fun getAllMemories(): LiveData<List<Memory>>
}
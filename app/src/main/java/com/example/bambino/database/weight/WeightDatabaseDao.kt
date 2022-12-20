package com.example.bambino.database.weight

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface WeightDatabaseDao {

    @Insert
    suspend fun insert(weight: Weight)

    @Query("DELETE FROM weight_table")
    suspend fun clear()

    @Query("SELECT * FROM weight_table ORDER BY measurement_date ASC")
    fun getAllMeasurements(): LiveData<List<Weight>>
}
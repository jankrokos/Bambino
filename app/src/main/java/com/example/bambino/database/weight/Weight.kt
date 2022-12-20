package com.example.bambino.database.weight

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weight_table")
data class Weight(
    @PrimaryKey(autoGenerate = false)
    var measurement_date: Long,

    @ColumnInfo(name = "measurement_result")
    var measurementResult: Double
)
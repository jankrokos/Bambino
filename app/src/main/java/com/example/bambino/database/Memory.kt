package com.example.bambino.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "memories_table")
data class Memory(
    @PrimaryKey(autoGenerate = true)
    var memoryId: Long = 0L,

    @ColumnInfo(name = "memory_photo_uri")
    var memoryPhotoUri: String,

    @ColumnInfo(name = "memory_date")
    var memoryDate: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "memory_description")
    var memoryDescription: String = ""

)

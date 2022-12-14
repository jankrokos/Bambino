package com.example.bambino.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "daily_tracked_actions_table")
data class TrackedAction(
    @PrimaryKey(autoGenerate = true)
    var actionId: Long = 0L,

    @ColumnInfo(name = "action_time")
    var actionTime: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "action_type")
    var actionType: String = "Default",

    @ColumnInfo(name = "action_humour")
    var actionHumour: Int = 3,

    @ColumnInfo(name = "action_description")
    var actionDescription: String = ""
)
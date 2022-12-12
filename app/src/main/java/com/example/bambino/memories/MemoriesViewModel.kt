package com.example.bambino.memories

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.example.bambino.database.MemoriesDatabaseDao

class MemoriesViewModel(
    val database: MemoriesDatabaseDao,
    application: Application
) : AndroidViewModel(application) {


}
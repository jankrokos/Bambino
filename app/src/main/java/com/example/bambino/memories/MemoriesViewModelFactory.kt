package com.example.bambino.memories

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bambino.database.MemoriesDatabaseDao
import com.example.bambino.track.TrackViewModel

class MemoriesViewModelFactory(
    private val dataSource: MemoriesDatabaseDao,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MemoriesViewModel::class.java)) {
            return MemoriesViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}
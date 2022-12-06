package com.example.bambino.action

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bambino.database.ActionsDatabaseDao
import com.example.bambino.track.TrackViewModel

class ActionViewModelFactory(
    private val dataSource: ActionsDatabaseDao
) : ViewModelProvider.Factory {
    @Suppress("unchecked cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ActionViewModel::class.java)) {
            return ActionViewModel(dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
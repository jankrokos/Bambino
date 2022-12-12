package com.example.bambino.memoentry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bambino.action.ActionViewModel
import com.example.bambino.database.ActionsDatabaseDao
import com.example.bambino.database.MemoriesDatabaseDao

class MemoryEntryViewModelFactory(
    private val dataSource: MemoriesDatabaseDao
) : ViewModelProvider.Factory {
    @Suppress("unchecked cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MemoryEntryViewModel::class.java)) {
            return MemoryEntryViewModel(dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
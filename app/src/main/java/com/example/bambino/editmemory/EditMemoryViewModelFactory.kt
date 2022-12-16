package com.example.bambino.editmemory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bambino.database.MemoriesDatabaseDao

class EditMemoryViewModelFactory(
    private val dataSource: MemoriesDatabaseDao,
    private val memoryKey: Long
) : ViewModelProvider.Factory {
    @Suppress("unchecked cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditMemoryViewModel::class.java)) {
            return EditMemoryViewModel(dataSource, memoryKey) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
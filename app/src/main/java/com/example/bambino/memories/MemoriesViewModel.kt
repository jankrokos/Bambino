package com.example.bambino.memories

import android.app.Application
import androidx.lifecycle.*
import com.example.bambino.database.MemoriesDatabaseDao
import kotlinx.coroutines.launch

class MemoriesViewModel(
    val database: MemoriesDatabaseDao,
    application: Application
) : AndroidViewModel(application) {

    private val _navigateToMemoryCreation = MutableLiveData<Boolean>()
    val navigateToMemoryCreation: LiveData<Boolean>
        get() = _navigateToMemoryCreation


    fun onNewMemory() {
        viewModelScope.launch {
            _navigateToMemoryCreation.value = true
        }
    }

    fun doneNavigating() {
        _navigateToMemoryCreation.value = false
    }


    //LIST OF MEMORIES
    val allMemories = database.getAllMemories()
}
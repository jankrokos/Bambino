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


    //ADDING NEW MEMORY
    fun onNewMemory() {
        viewModelScope.launch {
            _navigateToMemoryCreation.value = true
        }
    }

    fun doneNavigating() {
        _navigateToMemoryCreation.value = false
    }


    //EDIT EXISTING MEMORY
    private val _navigateToEditMemory = MutableLiveData<Long?>()
    val navigateToEditMemory
        get() = _navigateToEditMemory


    fun onEditMemoryClicked(id: Long) {
        _navigateToEditMemory.value = id
    }

    fun onEditMemoryNavigated() {
        _navigateToEditMemory.value = null
    }


    fun clear(): Boolean {
        viewModelScope.launch {
            database.clear()
        }
        return true
    }

    //LIST OF MEMORIES
    val allMemories = database.getAllMemories()
}
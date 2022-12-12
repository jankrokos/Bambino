package com.example.bambino.memoentry

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bambino.database.MemoriesDatabaseDao
import kotlinx.coroutines.launch

class MemoryEntryViewModel(
    val database: MemoriesDatabaseDao
) : ViewModel() {

    private val _navigateToMemoriesList = MutableLiveData<Boolean>()
    val navigateToMemoriesList: LiveData<Boolean>
        get() = _navigateToMemoriesList

    fun onAddMemory() {
        viewModelScope.launch {
            _navigateToMemoriesList.value = true
        }
    }

    fun doneNavigating() {
        _navigateToMemoriesList.value = false
    }

}
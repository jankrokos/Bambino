package com.example.bambino.editmemory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bambino.database.MemoriesDatabaseDao
import com.example.bambino.database.Memory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditMemoryViewModel(
    val database: MemoriesDatabaseDao,
    private val memoryKey: Long
) : ViewModel() {

    //NAVIGATION
    private val _navigateToMemoriesListDel = MutableLiveData<Boolean>()
    val navigateToMemoriesListDel: LiveData<Boolean>
        get() = _navigateToMemoriesListDel

    fun doneNavigatingDel() {
        _navigateToMemoriesListDel.value = false
    }

    private val _navigateToMemoriesListUp = MutableLiveData<Boolean>()
    val navigateToMemoriesListUp: LiveData<Boolean>
        get() = _navigateToMemoriesListUp

    fun doneNavigatingUp() {
        _navigateToMemoriesListUp.value = false
    }


    //UPDATE ENTRY
    fun onUpdate(uri: String, date: Long, description: String) {
        viewModelScope.launch {
            val updatedMemory = Memory()
            updatedMemory.memoryId = memoryKey
            updatedMemory.memoryPhotoUri = uri
            updatedMemory.memoryDate = date
            updatedMemory.memoryDescription = description
            updateMemory(updatedMemory)
            _navigateToMemoriesListUp.value = true
        }
    }

    private suspend fun updateMemory(memory: Memory) {
        withContext(Dispatchers.IO) {
            database.update(memory)
        }
    }

    var currentMemory = MutableLiveData<Memory?>()

    suspend fun getCurrentMemory(): Memory {
        return database.getMemoryWithId(memoryKey)
    }


    //DELETE ENTRY
    fun onDelete() {
        viewModelScope.launch {
            deleteMemory()
            _navigateToMemoriesListDel.value = true
        }
    }

    private suspend fun deleteMemory() {
        withContext(Dispatchers.IO) {
            database.delete(memoryKey)
        }
    }


    //PHOTO EDITION

    private val _memoryPhotoStringUri = MutableLiveData<String>()
    val memoryPhotoStringUri: LiveData<String>
        get() = _memoryPhotoStringUri

    private val _changePhoto = MutableLiveData<Boolean>()
    val changePhoto: LiveData<Boolean>
        get() = _changePhoto

    fun setMemoryPhotoUri(stringUri: String) {
        _memoryPhotoStringUri.value = stringUri
        _changePhoto.value = true
    }

    fun photoChanged() {
        _changePhoto.value = false
    }
}
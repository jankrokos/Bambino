package com.example.bambino.memoentry

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bambino.database.MemoriesDatabaseDao
import com.example.bambino.database.Memory
import com.example.bambino.database.TrackedAction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class MemoryEntryViewModel(
    val database: MemoriesDatabaseDao
) : ViewModel() {


    var dateStr: String = SimpleDateFormat("dd-MM-yyyy", Locale.UK)
        .format(System.currentTimeMillis()).toString()

    //NAVIGATION
    private val _navigateToMemoriesList = MutableLiveData<Boolean>()
    val navigateToMemoriesList: LiveData<Boolean>
        get() = _navigateToMemoriesList

    fun onAddMemory(memoryPhotoStringUri: String, memoryDate: Long, memoryDescription: String) {
        viewModelScope.launch {
            val memory = Memory()
            memory.memoryPhotoUri = memoryPhotoStringUri
            memory.memoryDate = memoryDate
            memory.memoryDescription = memoryDescription
            Log.i(
                "MemoEntry",
                "ADDING MEMORY: ${memory.memoryPhotoUri}, ${memory.memoryDate}, ${memory.memoryDescription}"
            )
            _navigateToMemoriesList.value = true
            insert(memory)
        }
    }

    fun doneNavigating() {
        _navigateToMemoriesList.value = false
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

    //DATABASE OPERATIONS
    private suspend fun clear() {
        withContext(Dispatchers.IO) {
            database.clear()
        }
    }

    private suspend fun insert(memory: Memory) {
        withContext(Dispatchers.IO) {
            database.insert(memory)
        }
    }


}
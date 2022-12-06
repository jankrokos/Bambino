package com.example.bambino.track

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.bambino.database.ActionsDatabaseDao
import com.example.bambino.database.TrackedAction
import kotlinx.coroutines.*

class TrackViewModel(
    val database: ActionsDatabaseDao,
    application: Application
) : AndroidViewModel(application) {

    private val _navigateToActionCreation = MutableLiveData<Boolean>()
    val navigateToActionCreation: LiveData<Boolean>
        get() = _navigateToActionCreation

    fun onNewAction() {
        viewModelScope.launch {
//            val newAction = TrackedAction()
//            insert(newAction)
            _navigateToActionCreation.value = true
        }
    }

    fun doneNavigating() {
        _navigateToActionCreation.value = false
    }



    val actions = database.getAllActions()

//    private var currentAction = MutableLiveData<TrackedAction?>()
//    init {
//        initializeCurrentAction()
//    }
//
//    private fun initializeCurrentAction() {
//        viewModelScope.launch {
//            currentAction.value = getCurrentActionFromDatabase()
//        }
//    }
//
//    private suspend fun getCurrentActionFromDatabase(): TrackedAction {
//        return database.getNewAction()
//    }

    private suspend fun clear() {
        withContext(Dispatchers.IO) {
            database.clear()
        }
    }

    private suspend fun update(action: TrackedAction) {
        withContext(Dispatchers.IO) {
            database.update(action)
        }
    }

    private suspend fun insert(action: TrackedAction) {
        withContext(Dispatchers.IO) {
            database.insert(action)
        }
    }

}


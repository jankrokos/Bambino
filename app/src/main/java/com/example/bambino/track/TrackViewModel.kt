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

    private var viewModelJob = Job()

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private val uiScope = CoroutineScope((Dispatchers.Main + viewModelJob))

    private val _navigateToActionCreation = MutableLiveData<Boolean>()
    val navigateToActionCreation: LiveData<Boolean>
        get() = _navigateToActionCreation

    fun onNewAction() {
        viewModelScope.launch {
            val newAction = TrackedAction()
            Log.i(
                "TrackViewModel",
                "newAction: ${newAction.actionId},${newAction.actionType},${newAction.actionTime}"
            )
            insert(newAction)
            Log.i("ActionsDatabaseDao", "XD")
            _navigateToActionCreation.value = true
        }
    }

    fun doneNavigating() {
        _navigateToActionCreation.value = false
    }

    //added vvv

    private var currentAction = MutableLiveData<TrackedAction?>()

    val actions = database.getAllActions()

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


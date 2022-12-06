package com.example.bambino.track

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
        val newAction = TrackedAction()
        Log.i(
            "TrackViewModel",
            "newAction: ${newAction.actionId},${newAction.actionType},${newAction.actionTime}"
        )
        database.insert(newAction)
        _navigateToActionCreation.value = true
    }

    fun doneNavigating() {
        _navigateToActionCreation.value = false
    }

    //added vvv

    private var currentAction = MutableLiveData<TrackedAction?>()

    private val actions = database.getAllActions()

    //works ^^

//    XDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD

//    init {
//        initializeCurrentAction()
//    }
//
//    private fun initializeCurrentAction() {
//        uiScope.launch {
//            currentAction.value = getCurrentActionFromDatabase()
//        }
//    }
//
//    private suspend fun getCurrentActionFromDatabase(): TrackedAction? {
//        var action = database.getNewAction()
//        if (action?.actionType != "Default") {
//            action = null
//        }
//        return action
//    }



}


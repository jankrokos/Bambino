package com.example.bambino.action

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bambino.database.ActionsDatabaseDao

class ActionViewModel(
    private val trackedActionKey: Long = 0L,
    val database: ActionsDatabaseDao
) : ViewModel() {
    private val _navigateToTrackList = MutableLiveData<Boolean>()
    val navigateToTrackList: LiveData<Boolean>
        get() = _navigateToTrackList


    fun onAddAction(actionTime: Long, actionType: String) {
        val action = database.get(trackedActionKey)
        action.actionTime = actionTime
        action.actionType = actionType
        _navigateToTrackList.value = true
    }

    fun onAddAction(){
        _navigateToTrackList.value = true
    }

    fun doneNavigating() {
        _navigateToTrackList.value = false
    }

}

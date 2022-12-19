package com.example.bambino.action

import android.accounts.AuthenticatorDescription
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bambino.database.ActionsDatabaseDao
import com.example.bambino.database.TrackedAction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ActionViewModel(
    val database: ActionsDatabaseDao
) : ViewModel() {

    private val _navigateToTrackList = MutableLiveData<Boolean>()
    val navigateToTrackList: LiveData<Boolean>
        get() = _navigateToTrackList


    suspend fun onAddAction(
        actionTime: Long,
        actionType: String,
        humour: Int,
        description: String
    ) {
        viewModelScope.launch {
            val action = TrackedAction()
            action.actionTime = actionTime
            action.actionType = actionType
            action.actionHumour = humour
            action.actionDescription = description
            _navigateToTrackList.value = true
            insert(action)
        }
    }

    fun doneNavigating() {
        _navigateToTrackList.value = false
    }

    private suspend fun insert(action: TrackedAction) {
        withContext(Dispatchers.IO) {
            database.insert(action)
        }
    }

}

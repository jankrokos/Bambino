package com.example.bambino.track

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.bambino.database.ActionsDatabaseDao

class TrackViewModel(
    val database: ActionsDatabaseDao,
    application: Application
) : AndroidViewModel(application) {

    private val _navigateToActionCreation = MutableLiveData<Boolean>()
    val navigateToSleepQuality: LiveData<Boolean>
        get() = _navigateToActionCreation


    fun onAddAction() {
        _navigateToActionCreation.value = true
    }

    fun doneNavigating() {
        _navigateToActionCreation.value = false
    }

}


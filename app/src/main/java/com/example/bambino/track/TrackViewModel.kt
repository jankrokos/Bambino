package com.example.bambino.track


import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.bambino.MainActivity
import com.example.bambino.database.ActionsDatabaseDao
import com.example.bambino.database.TrackedAction
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*


class TrackViewModel(
    val database: ActionsDatabaseDao,
    application: Application
) : AndroidViewModel(application) {

    private val activity = MainActivity()

    private val _navigateToActionCreation = MutableLiveData<Boolean>()
    val navigateToActionCreation: LiveData<Boolean>
        get() = _navigateToActionCreation

    val currentTime = System.currentTimeMillis()
    var currentDay = System.currentTimeMillis()
    var dayEnd: Long = 86400000 + currentDay


    fun setToday(date: Long) {
        currentDay = date
        dayEnd = 86400000 + currentDay
    }

    fun onNewAction() {
        viewModelScope.launch {
            _navigateToActionCreation.value = true
        }
        Log.i("TrackFragment", "Between $currentDay and ${currentDay + 86400000}")
    }

    fun clear(): Boolean {
        viewModelScope.launch {
//            database.clear()
        }
        return true
    }


    fun doneNavigating() {
        _navigateToActionCreation.value = false
    }

    val actions = database.getAllActions()

    var currentDayActions = database.getTodayActions(currentDay, dayEnd)

    fun filterDatabase(dayStart: Long, dayEnd: Long): LiveData<List<TrackedAction>> {
        return database.getTodayActions(dayStart, dayEnd)
    }

}


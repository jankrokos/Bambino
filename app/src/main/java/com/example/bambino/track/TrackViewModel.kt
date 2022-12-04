package com.example.bambino.track

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.bambino.database.ActionsDatabaseDao

class TrackViewModel(
    val database: ActionsDatabaseDao,
    application: Application) : AndroidViewModel(application)


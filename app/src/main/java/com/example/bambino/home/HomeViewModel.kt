package com.example.bambino.home

import android.app.Application
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bambino.database.MemoriesDatabaseDao
import com.example.bambino.database.weight.Weight
import com.example.bambino.database.weight.WeightDatabaseDao
import com.example.bambino.memories.MemoriesViewModel

class HomeViewModel(
    val database: WeightDatabaseDao
) : ViewModel() {







    suspend fun addNewMeasurement(date: Long, weight: Double) {
        val newWeight = Weight(measurement_date = date, measurementResult = weight)
        database.insert(newWeight)
    }

    suspend fun clearChart() {
        database.clear()
    }

    val allMeasurements = database.getAllMeasurements()

}


class HomeViewModelFactory(
    private val dataSource: WeightDatabaseDao,
) : ViewModelProvider.Factory {
    @Suppress("unchecked cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
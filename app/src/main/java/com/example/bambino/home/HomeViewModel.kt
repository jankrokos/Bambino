package com.example.bambino.home

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel() : ViewModel() {

    private val _familyPhotoUri = MutableLiveData<Uri>()
    val familyPhotoUri: LiveData<Uri>
        get() = _familyPhotoUri

    private val _changePhoto = MutableLiveData<Boolean>()
    val changePhoto: LiveData<Boolean>
        get() = _changePhoto


    fun setFamilyPhotoUri(uri: Uri) {
        _familyPhotoUri.value = uri
        _changePhoto.value = true
    }

    fun photoChanged() {
        _changePhoto.value = false
    }

}
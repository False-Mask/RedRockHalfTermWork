package com.example.redrockhalftermwork.jetpack

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData

class DetailedListModel(application: Application) : AndroidViewModel(application) {
    private val _tasks:MutableLiveData<Tasks> = MutableLiveData()

    private val tasks:LiveData<Tasks>
    get() {
        return _tasks
    }
}
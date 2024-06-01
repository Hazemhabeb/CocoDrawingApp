package com.task.cocoapp.ui.base


import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.task.cocoapp.data.repository.DataRepository
import com.task.cocoapp.ui.main.viewmodel.DataViewModel


class DataViewModelFactory constructor(private val repository: DataRepository, private val app:Application) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(DataViewModel::class.java)) {
            DataViewModel(this.repository,app) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}
package com.task.cocoapp.ui.base


import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.task.cocoapp.data.repository.DataRepository
import com.task.cocoapp.data.repository.ImageRepository
import com.task.cocoapp.ui.main.viewmodel.DataViewModel
import com.task.cocoapp.ui.main.viewmodel.ImageViewModel


class ImageViewModelFactory constructor(private val repository: ImageRepository, private val app:Application) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(ImageViewModel::class.java)) {
            ImageViewModel(this.repository,app) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}
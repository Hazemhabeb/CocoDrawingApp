package com.task.cocoapp.ui.base


import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.task.cocoapp.data.repository.DataRepository
import com.task.cocoapp.data.repository.ImageRepository
import com.task.cocoapp.data.repository.SegmentRepository
import com.task.cocoapp.ui.main.viewmodel.DataViewModel
import com.task.cocoapp.ui.main.viewmodel.ImageViewModel
import com.task.cocoapp.ui.main.viewmodel.SegmentViewModel


class SegmentViewModelFactory constructor(private val repository: SegmentRepository, private val app:Application) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(SegmentViewModel::class.java)) {
            SegmentViewModel(this.repository,app) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}
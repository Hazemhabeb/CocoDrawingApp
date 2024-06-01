package com.task.cocoapp.ui.main.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.task.cocoapp.data.model.ImageResponse
import com.task.cocoapp.data.repository.DataRepository
import com.task.cocoapp.data.repository.ImageRepository
import kotlinx.coroutines.*

class ImageViewModel constructor(private val imageRepository: ImageRepository, app:Application)  :
    AndroidViewModel(app) {

    val errorMessage = MutableLiveData<String>()
    val data = MutableLiveData<ArrayList<ImageResponse>>()
    var job: Job? = null

    //    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
//        onError("Exception handled: ${throwable.localizedMessage}")
//    }
    val loading = MutableLiveData<Boolean>()

    fun getData(queryType: String,images: ArrayList<String>) {
        loading.postValue(true)
        job = CoroutineScope(Dispatchers.IO ).launch {
            val response = imageRepository.getImages(queryType,images)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    data.postValue(response.body()!!)
                    loading.value = false
                } else {
                    onError("Error : ${response.message()} ")
                }
            }
        }
    }

    private fun onError(message: String) {
        errorMessage.value = message
        loading.value = false
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}
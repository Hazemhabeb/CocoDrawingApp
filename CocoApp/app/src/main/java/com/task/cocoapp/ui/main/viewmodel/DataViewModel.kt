package com.task.cocoapp.ui.main.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.task.cocoapp.data.repository.DataRepository
import kotlinx.coroutines.*

class DataViewModel constructor(private val dataRepository: DataRepository, app:Application)  :
    AndroidViewModel(app) {

    val errorMessage = MutableLiveData<String>()
    val data = MutableLiveData<ArrayList<Int>>()
    var job: Job? = null

    //    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
//        onError("Exception handled: ${throwable.localizedMessage}")
//    }
    val loading = MutableLiveData<Boolean>()

    fun getData(queryType: String,categ: ArrayList<String>) {
        loading.postValue(true)
        job = CoroutineScope(Dispatchers.IO ).launch {
            val response = dataRepository.getData(queryType,categ)
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
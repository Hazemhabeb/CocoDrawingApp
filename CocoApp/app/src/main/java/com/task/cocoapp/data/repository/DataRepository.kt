package com.task.cocoapp.data.repository

import com.task.cocoapp.data.api.RetrofitService

class DataRepository constructor(
    private val retrofitService: RetrofitService
) {

    suspend fun getData(queryType: String,categ: ArrayList<String>
    ) = retrofitService.getDataByCateg(queryType,categ)

}
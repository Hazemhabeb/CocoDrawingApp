package com.task.cocoapp.data.repository

import com.task.cocoapp.data.api.RetrofitService

class CaptionRepository constructor(
    private val retrofitService: RetrofitService
) {

    suspend fun getCaption(queryType: String,imageIds: ArrayList<String>
    ) = retrofitService.getCaption(queryType,imageIds)

}
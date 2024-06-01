package com.task.cocoapp.data.repository

import com.task.cocoapp.data.api.RetrofitService

class ImageRepository constructor(
    private val retrofitService: RetrofitService
) {

    suspend fun getImages(queryType: String,imageIds: ArrayList<String>
    ) = retrofitService.getImagesById(queryType,imageIds)

}
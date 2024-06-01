package com.task.cocoapp.data.repository

import com.task.cocoapp.data.api.RetrofitService

class SegmentRepository constructor(
    private val retrofitService: RetrofitService
) {

    suspend fun getSegment(queryType: String,imageIds: ArrayList<String>
    ) = retrofitService.getSegmentation(queryType,imageIds)

}
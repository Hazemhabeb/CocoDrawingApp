package com.task.cocoapp.data.model


import kotlinx.android.parcel.Parcelize
import org.json.JSONArray
import org.json.JSONObject


data class SegmentResponse(
    val image_id: Int = 0,
    val segmentation :String ,
    val category_id : Int = 0
    )



package com.task.cocoapp.data.api

import com.example.druggps.utils.BASE_URL
import com.example.druggps.utils.ServerTimeOut
import com.task.cocoapp.data.model.CaptionResponse
import com.task.cocoapp.data.model.ImageResponse
import com.task.cocoapp.data.model.SegmentResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

interface RetrofitService {


    companion object {

        var retrofitService: RetrofitService? = null

        fun getInstance(): RetrofitService {
            val interceptor = HttpLoggingInterceptor();
            interceptor.level = HttpLoggingInterceptor.Level.BODY


            val okHttpClient = OkHttpClient.Builder()
                .writeTimeout(ServerTimeOut.toLong(), TimeUnit.SECONDS)
                .readTimeout(ServerTimeOut.toLong(), TimeUnit.SECONDS)
                .connectTimeout(ServerTimeOut.toLong(), TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build()

            if (retrofitService == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                retrofitService = retrofit.create(RetrofitService::class.java)
            }
            return retrofitService!!
        }
    }



    @FormUrlEncoded
    @POST("/coco-dataset-bigquery")
    suspend fun getDataByCateg(@Field("querytype") querytype : String
                               ,@Field("category_ids[]") categ : ArrayList<String>): Response<ArrayList<Int>>

    @FormUrlEncoded
    @POST("/coco-dataset-bigquery")
    suspend fun getImagesById(@Field("querytype") querytype : String
                               ,@Field("image_ids[]") images : ArrayList<String>): Response<ArrayList<ImageResponse>>
    @FormUrlEncoded
    @POST("/coco-dataset-bigquery")
    suspend fun getSegmentation(@Field("querytype") querytype : String
                              ,@Field("image_ids[]") images : ArrayList<String>): Response<ArrayList<SegmentResponse>>
    @FormUrlEncoded
    @POST("/coco-dataset-bigquery")
    suspend fun getCaption(@Field("querytype") querytype : String
                                ,@Field("image_ids[]") images : ArrayList<String>): Response<ArrayList<CaptionResponse>>


}
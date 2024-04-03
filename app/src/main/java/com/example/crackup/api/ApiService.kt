package com.example.crackup.api

import com.example.crackup.model.reponse.*
import com.example.crackup.model.request.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("/")
    fun healthCheck(): Call<HealthResponse>

    @GET("/videos")
    fun getVideos(): Call<List<VideosResponse>>

    @POST("/upload")
    fun uploadVideo(@Body request: UploadRequest): Call<Unit>
}

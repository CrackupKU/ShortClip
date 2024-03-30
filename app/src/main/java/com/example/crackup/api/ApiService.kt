package com.example.crackup.api

import com.example.crackup.model.request.UploadRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("/")
    fun healthCheck(): Call<HealthResponse>

    @GET("/videos")
    fun getVideos(): Call<List<VideoUpload>>

    @POST("/upload")
    fun uploadVideo(@Body request: UploadRequest): Call<Unit>
}

data class HealthResponse(val message: String)

data class VideoUpload(
    val title: String,
    val videoUrl: String,
    val isAds: Boolean
)

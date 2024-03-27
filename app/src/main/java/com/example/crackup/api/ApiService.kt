package com.example.crackup.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("/")
    fun healthCheck(): Call<HealthResponse>

    @GET("/videos")
    fun getVideos(): Call<List<VideoUpload>>
}

data class HealthResponse(val message: String)

data class VideoUpload(
    val title: String,
    val videoUrl: String,
    val isAds: Boolean
)

package com.example.crackup.api

import com.example.crackup.model.Status
import com.example.crackup.model.UserModel
import com.example.crackup.model.reponse.*
import com.example.crackup.model.request.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @GET("/")
    fun healthCheck(): Call<HealthResponse>

    @GET("/users/{user_id}")
    fun getUser(@Path("user_id") userId: String): Call<UserModel>

    @GET("/videos")
    fun getVideos(): Call<List<VideosResponse>>

    @GET("/videos/user/{user_id}")
    fun getVideosByUser(@Path("user_id") userId: String): Call<List<VideosResponse>>

    @GET("/videos/user/{user_id}/status/{status}")
    fun getUserVideoByStatus(@Path("user_id") userId: String, @Path("status") status: Status): Call<List<VideosResponse>>

    @POST("/upload")
    fun uploadVideo(@Body request: UploadRequest): Call<Unit>
}

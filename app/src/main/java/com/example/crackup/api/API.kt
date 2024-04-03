package com.example.crackup.api

import com.example.crackup.model.reponse.*
import com.example.crackup.model.request.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object API {

    fun getHealth() {
        val call: Call<HealthResponse> = RetrofitClient.apiService.healthCheck()
        call.enqueue(object : Callback<HealthResponse> {
            override fun onResponse(
                call: Call<HealthResponse>, response: Response<HealthResponse>
            ) {
                if (response.isSuccessful) {
                    val healthResponse: HealthResponse? = response.body()
                    println("Health Status: ${healthResponse?.message}")
                } else {
                    println("Failed to fetch data: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<HealthResponse>, t: Throwable) {
                println("Network error: ${t.message}")
            }
        })
    }

    fun getVideos() {
        val call: Call<List<VideosResponse>> = RetrofitClient.apiService.getVideos()
        call.enqueue(object : Callback<List<VideosResponse>> {
            override fun onResponse(
                call: Call<List<VideosResponse>>, response: Response<List<VideosResponse>>
            ) {
                if (response.isSuccessful) {
                    val videos: List<VideosResponse>? = response.body()
                    videos?.forEach { video ->
                        println(video)
                    }
                } else {
                    println("Failed to fetch videos: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<VideosResponse>>, t: Throwable) {
                println("Network error: ${t.message}")
            }
        })
    }

    fun uploadVideo(request: UploadRequest) {
        val call = RetrofitClient.apiService.uploadVideo(request)
        call.enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (response.isSuccessful) {
                    // Request successful, handle the response
                    println("Upload successful")
                } else {
                    // Request failed, handle the error
                    println("Upload failed: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                // Request failed due to network error or other issues
                println("Network error: ${t.message}")
            }
        })
    }
}

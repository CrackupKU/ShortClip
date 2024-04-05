package com.example.crackup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.crackup.adapter.VideoPagerAdapter
import com.example.crackup.api.RetrofitClient
import com.example.crackup.databinding.ActivityMainBinding
import com.example.crackup.model.ShortVideo
import com.example.crackup.model.reponse.VideosResponse
import com.example.crackup.util.UiUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: VideoPagerAdapter
    private val homeVideos = ArrayList<VideosResponse>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        UiUtil.bindingNavBar(this, binding.bottomNavbar)
        setupViewPager()
    }

    private fun setupViewPager() {
        val call: Call<List<VideosResponse>> = RetrofitClient.apiService.getVideos()
        call.enqueue(object : Callback<List<VideosResponse>> {
            override fun onResponse(
                call: Call<List<VideosResponse>>, response: Response<List<VideosResponse>>
            ) {
                if (response.isSuccessful) {
                    val videos: List<VideosResponse>? = response.body()
                    videos?.forEach { video ->
                        homeVideos.add(video)
                    }
                    adapter = VideoPagerAdapter(homeVideos)
                    binding.viewPager.adapter = adapter
                } else {
                    Log.i("MainActivity", "Failed to fetch videos: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<VideosResponse>>, t: Throwable) {
                Log.i("MainActivity", "Network error: ${t.message}")
            }
        })

    }
}
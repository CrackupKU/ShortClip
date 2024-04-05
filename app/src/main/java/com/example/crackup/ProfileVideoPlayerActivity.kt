package com.example.crackup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.crackup.adapter.VideoPagerAdapter
import com.example.crackup.api.RetrofitClient
import com.example.crackup.databinding.ActivityProfileVideoPlayerBinding
import com.example.crackup.model.reponse.VideosResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileVideoPlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileVideoPlayerBinding
    private lateinit var adapter: VideoPagerAdapter
    private lateinit var videoId: String
    private val profileVideos = ArrayList<VideosResponse>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileVideoPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        videoId = intent.getStringExtra("videoId")!!
        setupViewPager()
    }

    private fun setupViewPager() {
        val call: Call<VideosResponse> = RetrofitClient.apiService.getVideo(videoId)
        call.enqueue(object : Callback<VideosResponse> {
            override fun onResponse(
                call: Call<VideosResponse>, response: Response<VideosResponse>
            ) {
                if (response.isSuccessful) {
                    val video: VideosResponse? = response.body()
                    video?.apply {
                        profileVideos.add(this)
                    }

                    adapter = VideoPagerAdapter(profileVideos)
                    binding.viewPager.adapter = adapter
                } else {
                    Log.i("MainActivity", "Failed to fetch videos: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<VideosResponse>, t: Throwable) {
                Log.i("MainActivity", "Network error: ${t.message}")
            }
        })
    }
}
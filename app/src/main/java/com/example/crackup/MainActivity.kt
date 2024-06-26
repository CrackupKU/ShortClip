package com.example.crackup

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.example.crackup.adapter.VideoPagerAdapter
import com.example.crackup.api.RetrofitClient
import com.example.crackup.databinding.ActivityMainBinding
import com.example.crackup.model.EmotionWatchTimeEntry
import com.example.crackup.model.reponse.VideosResponse
import com.example.crackup.model.request.RecommendRequest
import com.example.crackup.util.UiUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var adapter: VideoPagerAdapter? = null
    private val homeVideos = ArrayList<VideosResponse>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewPager()
    }

    private fun setupViewPager() {
        val requestBody = RecommendRequest(
            watchedTime = ArrayList<EmotionWatchTimeEntry>(),
            boundVideoIds = ArrayList<String>()
        )
        val call: Call<List<VideosResponse>> =
            RetrofitClient.apiService.getRecommendedVideos(requestBody)
        call.enqueue(object : Callback<List<VideosResponse>> {
            override fun onResponse(
                call: Call<List<VideosResponse>>, response: Response<List<VideosResponse>>
            ) {
                if (response.isSuccessful) {
                    val videos: List<VideosResponse>? = response.body()
                    videos?.forEach { video ->
                        homeVideos.add(video)
                    }
                    Log.i("MainActivity", videos.toString())
                    adapter = VideoPagerAdapter(homeVideos)
                    UiUtil.bindingNavBar(this@MainActivity, binding.bottomNavbar, adapter)
                    binding.viewPager.adapter = adapter
                    binding.viewPager.registerOnPageChangeCallback(
                        object : OnPageChangeCallback() {
                            override fun onPageSelected(position: Int) {
                                adapter?.timer?.cancel()
                                adapter?.timer = null
                            }
                        }
                    )
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
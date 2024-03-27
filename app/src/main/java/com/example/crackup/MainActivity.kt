package com.example.crackup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.crackup.adapter.VideoPagerAdapter
import com.example.crackup.databinding.ActivityMainBinding
import com.example.crackup.model.ShortVideo
import com.example.crackup.util.FuncUtil

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: VideoPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        FuncUtil.bindingNavBar(this, binding.bottomNavbar)
        setupViewPager()

    }

    private fun setupViewPager() {
        val shortVideos = listOf(
            ShortVideo(
                "Video 1",
                "https://drive.google.com/uc?export=download&id=1pjtg7rNYITvkwhhcjAkWMjDZsV8iZSo0"
            ),
            ShortVideo(
                "Video 2",
                "https://drive.google.com/uc?export=download&id=1X6yCOPkO2d8NLIVI66wbcRYOtrgB-gBU"
            ),
            // Add more short videos as needed
        )
        adapter = VideoPagerAdapter(shortVideos)
        binding.viewPager.adapter = adapter
    }
}
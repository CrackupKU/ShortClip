package com.example.crackup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.crackup.adapter.VideoPagerAdapter
import com.example.crackup.databinding.ActivityMainBinding
import com.example.crackup.model.ShortVideo
import com.example.crackup.util.UiUtil

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: VideoPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavbar.setOnItemSelectedListener { menuItem ->
            when(menuItem.itemId) {
                R.id.bottom_menu_home -> {
                    UiUtil.showToast(this,"Home")
                }
                R.id.bottom_menu_add_video->{
                    UiUtil.showToast(this,"Add video")
                    // Go to add video page
                    startActivity(Intent(this,VideoUploadActivity::class.java))
                }
                R.id.bottom_menu_profile->{
                    UiUtil.showToast(this,"Profile")
                    // Go to profile page
                }
            }
            false
        }
        setupViewPager()

    }

    private fun setupViewPager(){
        val shortVideos = listOf(
            ShortVideo("Video 1", "https://drive.google.com/uc?export=download&id=1pjtg7rNYITvkwhhcjAkWMjDZsV8iZSo0"),
            ShortVideo("Video 2", "https://drive.google.com/uc?export=download&id=1X6yCOPkO2d8NLIVI66wbcRYOtrgB-gBU"),
            // Add more short videos as needed
        )
        adapter = VideoPagerAdapter(shortVideos)
        binding.viewPager.adapter = adapter
    }
}
package com.example.crackup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.crackup.databinding.ActivityMainBinding
import com.example.crackup.util.UiUtil

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

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
                }
                R.id.bottom_menu_profile->{
                    UiUtil.showToast(this,"Profile")
                    // Go to profile page
                }
            }
            false
        }
    }
}
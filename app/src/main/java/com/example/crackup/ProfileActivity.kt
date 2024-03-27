package com.example.crackup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.crackup.databinding.ActivityMainBinding
import com.example.crackup.databinding.ActivityProfileBinding
import com.example.crackup.util.UiUtil

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        UiUtil.bindingNavBar(this, binding.bottomNavbar)
    }
}
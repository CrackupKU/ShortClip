package com.example.crackup.util

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.crackup.MainActivity
import com.example.crackup.ProfileActivity
import com.example.crackup.R
import com.example.crackup.VideoUploadActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

object UiUtil {

    fun bindingNavBar(activity: AppCompatActivity, bottomNavigationView: BottomNavigationView) {
        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.bottom_menu_home -> {
                    UiUtil.showToast(activity, "Home")
                    activity.startActivity(Intent(activity, MainActivity::class.java))
                }

                R.id.bottom_menu_add_video -> {
                    UiUtil.showToast(activity, "Add video")
                    // Go to add video page
                    activity.startActivity(Intent(activity, VideoUploadActivity::class.java))
                }

                R.id.bottom_menu_profile -> {
                    UiUtil.showToast(activity, "Profile")
                    // Go to profile page
                    activity.startActivity(Intent(activity, ProfileActivity::class.java))
                }
            }
            false
        }
    }

    fun showToast(context : Context,message : String){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
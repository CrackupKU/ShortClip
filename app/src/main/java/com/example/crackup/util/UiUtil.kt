package com.example.crackup.util

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.crackup.ProfileActivity
import com.example.crackup.R
import com.example.crackup.VideoUploadActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

object UiUtil {

    fun bindingNavBar(activity: AppCompatActivity, bottomNavigationView: BottomNavigationView) {
        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.bottom_menu_home -> {
                    showToast(activity, "Home")
                }

                R.id.bottom_menu_add_video -> {
                    showToast(activity, "Upload")
                    // Go to add video page
                    activity.startActivity(Intent(activity, VideoUploadActivity::class.java))
                }

                R.id.bottom_menu_profile -> {
                    showToast(activity, "Profile")
                    // Go to profile page
                    val intent = Intent(activity, ProfileActivity::class.java)
                    intent.putExtra("profile_user_id", FirebaseAuth.getInstance().currentUser?.uid)
                    activity.startActivity(intent)
                }
            }
            false
        }
    }

    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
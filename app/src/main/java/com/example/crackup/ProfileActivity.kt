package com.example.crackup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.crackup.api.RetrofitClient
import com.example.crackup.databinding.ActivityProfileBinding
import com.example.crackup.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var profileUserId: String
    private lateinit var currentUserId: String
    private lateinit var profileUserModel: UserModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        profileUserId = intent.getStringExtra("profile_user_id")!!
        currentUserId = FirebaseAuth.getInstance().currentUser?.uid!!

        if (profileUserId == currentUserId) {
            binding.profileBtn.text = "Logout"
            binding.profileBtn.setOnClickListener {
                logout()
            }
        }

        getProfileDataFromFirebase()
    }

    private fun logout() {
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun getProfileDataFromFirebase() {
        val call: Call<UserModel> = RetrofitClient.apiService.getUser(profileUserId)
        call.enqueue(object : Callback<UserModel> {
            override fun onResponse(call: Call<UserModel>, response: Response<UserModel>) {
                if (response.isSuccessful) {
                    val user: UserModel? = response.body()
                    if (user != null) {
                        profileUserModel = user
                        setUI()
                    }
                } else {
                    Log.i("ProfileActivity", "Failed to user videos: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<UserModel>, t: Throwable) {
                Log.i("ProfileActivity", "Network error: ${t.message}")
            }
        })

    }

    private fun setUI() {
        profileUserModel.apply {
            Glide.with(binding.profilePic).load(profilePic)
                .apply(RequestOptions().placeholder(R.drawable.icon_profile_circle))
                .circleCrop()
                .into(binding.profilePic)
            binding.profileUsername.text = "@" + username
            binding.progressBar.visibility = View.INVISIBLE
            binding.followingCount.text = followingList.size.toString()
            binding.followerCount.text = followerList.size.toString()
            binding.postCount.text = uploadVideo.size.toString()
            // TODO: Fetch video by user HERE
        }
    }
}
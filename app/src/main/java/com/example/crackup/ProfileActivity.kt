package com.example.crackup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.crackup.adapter.ProfileVideoAdapter
import com.example.crackup.adapter.VideoPagerAdapter
import com.example.crackup.api.RetrofitClient
import com.example.crackup.databinding.ActivityProfileBinding
import com.example.crackup.model.Status
import com.example.crackup.model.UserModel
import com.example.crackup.model.VideoModel
import com.example.crackup.model.reponse.VideosResponse
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var adapter: ProfileVideoAdapter
    private lateinit var profileUserId: String
    private lateinit var currentUserId: String
    private lateinit var profileUser: UserModel
    private val profileUserVideos = ArrayList<VideosResponse>()

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
                        profileUser = user
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
        profileUser.apply {
            binding.progressBar.visibility = View.GONE
            Glide.with(binding.profilePic).load(profilePic)
                .apply(RequestOptions().placeholder(R.drawable.icon_profile_circle))
                .circleCrop()
                .into(binding.profilePic)
            binding.profileUsername.text = "@$username"
            binding.followingCount.text = followingList.size.toString()
            binding.followerCount.text = followerList.size.toString()
            setUpRecyclerViewForDisplayUserVideos()
        }
    }

    private fun setUpRecyclerViewForDisplayUserVideos() {
        val call: Call<List<VideosResponse>> =
            RetrofitClient.apiService.getUserVideoByStatus(profileUser.id, Status.PUBLISH)
        call.enqueue(object : Callback<List<VideosResponse>> {
            override fun onResponse(
                call: Call<List<VideosResponse>>, response: Response<List<VideosResponse>>
            ) {
                if (response.isSuccessful) {
                    val videos: List<VideosResponse>? = response.body()
                    videos?.forEach { video ->
                        profileUserVideos.add(video)
                    }
                    adapter = ProfileVideoAdapter(profileUserVideos)
                    binding.postCount.text = profileUserVideos.size.toString()
                    binding.recyclerView.layoutManager = GridLayoutManager(this@ProfileActivity, 3)
                    binding.recyclerView.adapter = adapter
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
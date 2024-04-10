package com.example.crackup.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.crackup.ProfileActivity
import com.example.crackup.R
import com.example.crackup.api.RetrofitClient
import com.example.crackup.databinding.VideoPlayerBinding
import com.example.crackup.model.Emotion
import com.example.crackup.model.EmotionWatchTimeEntry
import com.example.crackup.model.UserModel
import com.example.crackup.model.reponse.VideosResponse
import com.example.crackup.model.request.RecommendRequest
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Timer
import java.util.TimerTask

class VideoPagerAdapter(private val shortVideos: ArrayList<VideosResponse>) :
    RecyclerView.Adapter<VideoPagerAdapter.VideoViewHolder>() {

    private val MAX_CAPTION_LENGTH = 35

    private var isExpanded = false
    var timer: Timer? = null
    private val boundVideoIds = ArrayList<String>()
    private val userEmotionWatchTime: ArrayList<EmotionWatchTimeEntry> = arrayListOf(
        EmotionWatchTimeEntry(Emotion.FEAR, 0),
        EmotionWatchTimeEntry(Emotion.HAPPY, 0),
        EmotionWatchTimeEntry(Emotion.ANGRY, 0),
        EmotionWatchTimeEntry(Emotion.SAD, 0),
        EmotionWatchTimeEntry(Emotion.DISGUST, 0),
        EmotionWatchTimeEntry(Emotion.SURPRISE, 0),
    )

    inner class VideoViewHolder(private val binding: VideoPlayerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindVideo(shortVideo: VideosResponse) {
            Firebase.firestore.collection("users")
                .document(shortVideo.uploadBy)
                .get().addOnSuccessListener {
                    val userModel = it?.toObject(UserModel::class.java)
                    userModel?.apply {
                        binding.usernameView.text = username
                        Glide.with(binding.profileIcon).load(profilePic)
                            .circleCrop()
                            .apply(
                                RequestOptions().placeholder(R.drawable.icon_profile_circle)
                            )
                            .into(binding.profileIcon)

                        binding.profileIcon.setOnClickListener {
                            timer?.cancel()
                            timer = null
                            val intent = Intent(binding.profileIcon.context, ProfileActivity::class.java)
                            intent.putExtra("profile_user_id", id )
                            binding.profileIcon.context.startActivity(intent)
                        }
                    }
                }

            if (!boundVideoIds.contains(shortVideo.id) && !shortVideo.isAds) {
                boundVideoIds.add(shortVideo.id)
                Log.i("VideoPagerAdapter", "boundVideoIds add ${shortVideo.id}")
            }
            binding.advertisementTextView.visibility =
                if (shortVideo.isAds) View.VISIBLE else View.GONE
            binding.titleView.text = "(${shortVideo.emotion}) ${shortVideo.title}"
            binding.progressBar.visibility = View.VISIBLE
            val caption = shortVideo.caption
            if (caption.length > MAX_CAPTION_LENGTH) {
                val truncatedCaption = caption.substring(0, MAX_CAPTION_LENGTH).trim() + " ..."
                binding.captionView.text = truncatedCaption
            } else {
                binding.captionView.text = caption
            }

            binding.videoView.apply {
                setVideoPath(shortVideo.videoUrl)
                setOnCompletionListener {
                    timer?.cancel()
                    timer = null
                }
                setOnPreparedListener {
                    binding.progressBar.visibility = View.GONE
                    it.start()
                    it.isLooping = true
                    timer = Timer()
                    startTimer(timer, shortVideo)
                }
                //play pause
                setOnClickListener {
                    if (isPlaying) {
                        pause()
                        binding.pauseIcon.visibility = View.VISIBLE
                        timer?.cancel()
                        timer = null
                    } else {
                        start()
                        binding.pauseIcon.visibility = View.GONE
                        timer = Timer()
                        startTimer(timer, shortVideo)
                    }
                }
            }
            binding.captionView.apply {
                setOnClickListener {
                    if (caption.length > MAX_CAPTION_LENGTH) {
                        if (isExpanded) {
                            val truncatedCaption =
                                caption.substring(0, MAX_CAPTION_LENGTH).trim() + " ..."
                            binding.captionView.text = truncatedCaption

                        } else {
                            binding.captionView.text = caption
                        }
                        isExpanded = !isExpanded
                    }
                }
            }
        }

        private fun addWatchTime(emotion: Emotion) {
            val entry = userEmotionWatchTime.find { it.emotion == emotion }
            entry?.duration = entry?.duration!! + 1
        }

        private fun startTimer(inputTimer: Timer?, shortVideo: VideosResponse) {
            inputTimer?.scheduleAtFixedRate(object : TimerTask() {
                override fun run() {
                    shortVideo.emotion?.let { it1 ->
                        addWatchTime(it1)
                        Log.i(
                            "VideoPagerAdapter",
                            "${userEmotionWatchTime.find { it.emotion == shortVideo.emotion }?.emotion}: ${userEmotionWatchTime.find { it.emotion == shortVideo.emotion }?.duration}"
                        )
                    }
                }
            }, 0, 1000) // ms
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val binding = VideoPlayerBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return VideoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        // Bind data to your item layout
        val shortVideo = shortVideos[position]

        if (shortVideo.isAds) {
            getMoreRecommends()
        }

        // Update item layout with the data
        holder.bindVideo(shortVideo)

    }

    override fun getItemCount(): Int {
        return shortVideos.size
    }

    private fun getMoreRecommends() {
        val requestBody = RecommendRequest(
            watchedTime = userEmotionWatchTime,
            boundVideoIds = boundVideoIds
        )
        Log.i("getMore", requestBody.toString())
        val call: Call<List<VideosResponse>> =
            RetrofitClient.apiService.getRecommendedVideos(requestBody)
        call.enqueue(object : Callback<List<VideosResponse>> {
            override fun onResponse(
                call: Call<List<VideosResponse>>, response: Response<List<VideosResponse>>
            ) {
                if (response.isSuccessful) {
                    val videos: List<VideosResponse>? = response.body()
                    videos?.forEach { video ->
                        shortVideos.add(video)
                    }
                } else {
                    Log.i("VideoPagerAdapter", "Failed to fetch videos: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<VideosResponse>>, t: Throwable) {
                Log.i("VideoPagerAdapter", "Network error: ${t.message}")
            }
        })
    }
}

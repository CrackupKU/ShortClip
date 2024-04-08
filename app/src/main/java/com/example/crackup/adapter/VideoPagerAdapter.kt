package com.example.crackup.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.crackup.R
import com.example.crackup.databinding.VideoPlayerBinding
import com.example.crackup.model.UserModel
import com.example.crackup.model.reponse.VideosResponse
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class VideoPagerAdapter(private val shortVideos: List<VideosResponse>) :
    RecyclerView.Adapter<VideoPagerAdapter.VideoViewHolder>() {

    private var isExpanded = false
    private val MAX_CAPTION_LENGTH = 35

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

                    }
                }

            binding.titleView.text = shortVideo.title
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
                setOnPreparedListener {
                    binding.progressBar.visibility = View.GONE
                    it.start()
                    it.isLooping = true
                }
                //play pause
                setOnClickListener {
                    if (isPlaying) {
                        pause()
                        binding.pauseIcon.visibility = View.VISIBLE
                    } else {
                        start()
                        binding.pauseIcon.visibility = View.GONE
                    }
                }
            }
            binding.captionView.apply {
                setOnClickListener {
                    if (caption.length > MAX_CAPTION_LENGTH) {
                        if (isExpanded) {
                            val truncatedCaption = caption.substring(0, MAX_CAPTION_LENGTH).trim() + " ..."
                            binding.captionView.text = truncatedCaption

                        } else {
                            binding.captionView.text = caption
                        }
                        isExpanded = !isExpanded
                    }
                }
            }
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

        // Update item layout with the data
        holder.bindVideo(shortVideo)
    }

    override fun getItemCount(): Int {
        return shortVideos.size
    }
}

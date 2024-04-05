package com.example.crackup.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.crackup.ProfileVideoPlayerActivity
import com.example.crackup.databinding.ProfileVideoItemRowBinding
import com.example.crackup.model.reponse.VideosResponse

class ProfileVideoAdapter(private val videos: List<VideosResponse>) :
    RecyclerView.Adapter<ProfileVideoAdapter.VideoViewHolder>() {

    inner class VideoViewHolder(private val binding: ProfileVideoItemRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindVideo(video: VideosResponse) {
            Glide.with(binding.thumbnailImageView).load(video.videoUrl)
                .into(binding.thumbnailImageView)
            binding.thumbnailImageView.setOnClickListener{
                val intent = Intent(binding.thumbnailImageView.context, ProfileVideoPlayerActivity::class.java)
                intent.putExtra("videoId", video.id)
                binding.thumbnailImageView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val binding = ProfileVideoItemRowBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return VideoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val video = videos[position]
        holder.bindVideo(video)
    }

    override fun getItemCount(): Int {
        return videos.size
    }
}
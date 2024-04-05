package com.example.crackup.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.crackup.databinding.VideoPlayerBinding
import com.example.crackup.model.ShortVideo

class VideoPagerAdapter(private val shortVideos: List<ShortVideo>) :
    RecyclerView.Adapter<VideoPagerAdapter.VideoViewHolder>() {

    inner class VideoViewHolder(private val binding: VideoPlayerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindVideo(shortVideo: ShortVideo) {
            binding.videoView.apply {
                setVideoPath(shortVideo.videoUrl)
                setOnPreparedListener {
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

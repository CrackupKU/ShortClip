package com.example.crackup.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.crackup.databinding.ProcessingVideoItemBinding
import com.example.crackup.model.reponse.VideosResponse

class ProcessingVideoAdapter(private val processingVideos: List<VideosResponse>) :
    RecyclerView.Adapter<ProcessingVideoAdapter.ProcessingVideoViewHolder>() {

    inner class ProcessingVideoViewHolder(private val binding: ProcessingVideoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindVideo(video: VideosResponse) {
            Glide.with(binding.thumbnailImageView).load(video.videoUrl)
                .into(binding.thumbnailImageView)
            binding.videoTitle.text = video.title
            binding.videoCaption.text = video.caption
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProcessingVideoViewHolder {
        val binding = ProcessingVideoItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProcessingVideoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProcessingVideoViewHolder, position: Int) {
        val video = processingVideos[position]
        holder.bindVideo(video)
    }

    override fun getItemCount(): Int {
        return processingVideos.size
    }
}
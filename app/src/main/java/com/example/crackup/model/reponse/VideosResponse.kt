package com.example.crackup.model.reponse

import com.example.crackup.model.*

data class VideosResponse(
    val id: String,
    val filename: String,
    val title: String,
    val caption: String,
    val videoUrl: String,
    val emotion: Emotion?,
    val status: Status,
    val isAds: Boolean,
    val processingUrl: String?,
    val emotionLength: List<String>?, // Change List<List<Float>>? to List<String>?
    val similarVideo: String?,
    val uploadBy: String,
    val uploadDate: String
)

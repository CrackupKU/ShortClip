package com.example.crackup.model

import com.google.firebase.Timestamp

data class VideoModel(
    var id: String = "",
    var filename: String = "",
    var title: String = "",
    var caption: String = "",
    var videoUrl: String = "",
    var emotion: Emotion? = null, // Allow null for Emotion
    var status: Status,
    var isAds: Boolean = false,
    var processingUrl: String = "",
    var emotionLength: List<String>? = emptyList(),
    var similarVideo: String = "",
    var uploadBy: String = "",
    var uploadDate: Timestamp = Timestamp.now(),
)

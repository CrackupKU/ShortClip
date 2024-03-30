package com.example.crackup.model

import com.google.firebase.Timestamp

data class VideoModel(
    var id: String = "",
    var title: String = "",
    var caption: String = "",
    var videoUrl: String = "",
    var emotion: Emotion,
    var status: Status,
    var isAds: Boolean = false,
    var uploadBy: String = "",
    var uploadDate: Timestamp = Timestamp.now(),
    var processingUrl: String = ""
)

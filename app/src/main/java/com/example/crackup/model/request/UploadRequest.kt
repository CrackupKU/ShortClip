package com.example.crackup.model.request

import java.time.LocalDateTime

data class UploadRequest(
    var title: String = "",
    var caption: String = "",
    var videoUrl: String = "",
    var isAds: Boolean = false,
    var uploadBy: String = "",
    var uploadDate: String = LocalDateTime.now().toString(),
)

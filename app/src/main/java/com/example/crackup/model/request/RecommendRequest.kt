package com.example.crackup.model.request

import com.example.crackup.model.EmotionWatchTimeEntry

data class RecommendRequest (
    val watchedTime: ArrayList<EmotionWatchTimeEntry>,
    val boundVideoIds: ArrayList<String>
)
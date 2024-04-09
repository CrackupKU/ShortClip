package com.example.crackup.util

import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import com.example.crackup.model.Emotion
import com.example.crackup.model.EmotionWatchTimeEntry

object FuncUtil {

    fun getVideoDuration(context: Context, uri: Uri): Long? {
        val retriever = MediaMetadataRetriever()
        try {
            retriever.setDataSource(context, uri)
            val durationString =
                retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
            return durationString?.toLong()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            retriever.release()
        }
        return null
    }

    // Retrieving a specific emotion's watch time
    fun getEmotionWatchTime(
        userEmotionWatchTime: ArrayList<EmotionWatchTimeEntry>,
        emotion: Emotion
    ): Long? {
        return userEmotionWatchTime.find { it.emotion == emotion }?.duration
    }

    // Updating a specific emotion's watch time
    fun updateEmotionWatchTime(
        userEmotionWatchTime: ArrayList<EmotionWatchTimeEntry>,
        emotion: Emotion,
        duration: Long
    ) {
        userEmotionWatchTime.find { it.emotion == emotion }?.duration =
            userEmotionWatchTime.find { it.emotion == emotion }?.duration?.plus(
                duration
            )!!
    }

}
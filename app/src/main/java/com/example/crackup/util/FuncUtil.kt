package com.example.crackup.util

import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.util.Log
import com.example.crackup.model.Emotion
import com.example.crackup.model.EmotionWatchTimeEntry
import com.example.crackup.model.reponse.VideosResponse
import java.util.Timer
import java.util.TimerTask

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
}
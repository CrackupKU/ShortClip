package com.example.crackup.util

import android.content.Context
import android.content.Intent
import android.media.MediaMetadataRetriever
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import com.example.crackup.MainActivity
import com.example.crackup.ProfileActivity
import com.example.crackup.R
import com.example.crackup.VideoUploadActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

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
package com.example.crackup

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.example.crackup.api.API
import com.example.crackup.databinding.ActivityPostVideoBinding
import com.example.crackup.model.request.UploadRequest
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage

class PostVideoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPostVideoBinding
    private var selectedVideoUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prepareUri()

        binding.submitPostBtn.setOnClickListener {
            postVideoHandler()
        }

        binding.cancelPostBtn.setOnClickListener {
            startActivity(Intent(this, VideoUploadActivity::class.java))
            finish()
        }
    }

    private fun prepareUri() {
        val videoUriString = intent.getStringExtra("videoUri")
        selectedVideoUri = if (videoUriString != null) Uri.parse(videoUriString) else null
        selectedVideoUri?.toString()?.let { Log.i("PostVideoActivity", it) }
        selectedVideoUri?.let {
            Glide.with(binding.postThumbnailView).load(it).into(binding.postThumbnailView)
        }
    }

    private fun postVideoHandler() {
        if (binding.postTitleInput.text.toString().isEmpty()) {
            binding.postTitleInput.error = "This field cannot be empty"
            return;
        }
        if (binding.postCaptionInput.text.toString().isEmpty()) {
            binding.postCaptionInput.error = "This field cannot be empty"
            return;
        }
        setPostInProgress(true);
        uploadToFirebaseStorage(selectedVideoUri)
    }

    private fun uploadToFirebaseStorage(videoUri: Uri?) {
        videoUri?.apply {
            val videoRef = FirebaseStorage.getInstance()
                .reference
                .child("videos/" + this.lastPathSegment)

            videoRef.putFile(this)
                .addOnSuccessListener {
                    videoRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                        val request =
                            UploadRequest(
                                title = binding.postTitleInput.text.toString(),
                                caption = binding.postCaptionInput.text.toString(),
                                videoUrl = downloadUrl.toString(),
                                isAds = false, // TODO: Need real isAds
                                uploadBy = FirebaseAuth.getInstance().currentUser?.uid ?: "",
                            )

                        API.uploadVideo(request)
                        setPostInProgress(false)
                        finish()
                    }
                }
        }
    }

    private fun setPostInProgress(inProgress: Boolean) {
        if (inProgress) {
            binding.progressBar.visibility = View.VISIBLE
            binding.submitPostBtn.visibility = View.GONE
        } else {
            binding.progressBar.visibility = View.GONE
            binding.submitPostBtn.visibility = View.VISIBLE
        }
    }
}
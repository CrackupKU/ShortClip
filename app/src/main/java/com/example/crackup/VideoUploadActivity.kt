package com.example.crackup

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.crackup.adapter.ProcessingVideoAdapter
import com.example.crackup.api.RetrofitClient
import com.example.crackup.databinding.ActivityVideoUploadBinding
import com.example.crackup.model.Status
import com.example.crackup.model.reponse.VideosResponse
import com.example.crackup.util.FuncUtil
import com.example.crackup.util.UiUtil
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VideoUploadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVideoUploadBinding
    private lateinit var adapter: ProcessingVideoAdapter
    private lateinit var currentUserId: String
    private var selectedVideoUri: Uri? = null
    private val processingVideos = ArrayList<VideosResponse>()
    private lateinit var videoLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)
        currentUserId = FirebaseAuth.getInstance().currentUser?.uid!!

        videoLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                selectedVideoUri = result.data?.data
                if (selectedVideoUri != null && isVideoDurationValid(selectedVideoUri!!)) {
                    startActivity(
                        Intent(this, PostVideoActivity::class.java)
                            .putExtra("videoUri", selectedVideoUri.toString())
                    )
                } else {
                    UiUtil.showToast(this, "Video duration must be no longer than 1 minute")
                }
            }
        }

        binding.uploadVideoView.createButton.setOnClickListener {
            checkPermissionAndOpenVideoPicker()
        }
        binding.uploadVideoView.backButton.setOnClickListener {
            finish()
        }
        setUpRecyclerViewForDisplayProcessingVideos()
    }

    private fun checkPermissionAndOpenVideoPicker() {
        val readExternalVideo =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {  // or Build.VERSION_CODES.R
                android.Manifest.permission.READ_MEDIA_VIDEO
            } else {
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            }
        if (ContextCompat.checkSelfPermission(
                this,
                readExternalVideo
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            openVideoPicker()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(readExternalVideo), 100)
        }
    }

    private fun openVideoPicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
        intent.type = "video/*"
        videoLauncher.launch(intent)
    }

    private fun isVideoDurationValid(videoUri: Uri): Boolean {
        val videoDuration = FuncUtil.getVideoDuration(this, videoUri) ?: 0
        return videoDuration <= 60000
    }

    private fun setUpRecyclerViewForDisplayProcessingVideos() {
        val call: Call<List<VideosResponse>> =
            RetrofitClient.apiService.getUserVideoByStatus(currentUserId, Status.PROCESS)
        call.enqueue(object : Callback<List<VideosResponse>> {
            override fun onResponse(
                call: Call<List<VideosResponse>>, response: Response<List<VideosResponse>>
            ) {
                if (response.isSuccessful) {
                    val videos: List<VideosResponse>? = response.body()
                    videos?.forEach { video ->
                        processingVideos.add(video)
                    }
                    adapter = ProcessingVideoAdapter(processingVideos)
                    binding.progressBarVideo.visibility = View.GONE
                    binding.processingVideoList.adapter = ProcessingVideoAdapter(processingVideos)
                    binding.processingVideoList.layoutManager =
                        LinearLayoutManager(this@VideoUploadActivity)
                } else {
                    Log.i("MainActivity", "Failed to fetch videos: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<VideosResponse>>, t: Throwable) {
                Log.i("MainActivity", "Network error: ${t.message}")
            }
        })
    }
}
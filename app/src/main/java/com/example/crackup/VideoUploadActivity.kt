package com.example.crackup

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.crackup.databinding.ActivityVideoUploadBinding
import com.example.crackup.util.UiUtil

class VideoUploadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVideoUploadBinding
    private var selectedVideoUri : Uri? = null
    private lateinit var videoLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        videoLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if(result.resultCode == RESULT_OK){
                selectedVideoUri = result.data?.data
                UiUtil.showToast(this,"Got video "+selectedVideoUri.toString())
            }
        }

        binding.uploadView.createButton.setOnClickListener {
            checkPermissionAndOpenVideoPicker()
        }

    }

    private fun checkPermissionAndOpenVideoPicker() {
        val readExternalVideo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {  // or Build.VERSION_CODES.R
            android.Manifest.permission.READ_MEDIA_VIDEO
        } else {
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        }
        if (ContextCompat.checkSelfPermission(this, readExternalVideo) == PackageManager.PERMISSION_GRANTED) {
            openVideoPicker()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(readExternalVideo), 100)
        }
    }


    private fun openVideoPicker() {
        var intent = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
        intent.type = "video/*"
        videoLauncher.launch(intent)
    }
}
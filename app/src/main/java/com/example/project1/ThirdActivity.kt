package com.example.project1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.MediaController
import android.widget.VideoView
import androidx.camera.core.*
import androidx.camera.core.R
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.example.project1.databinding.ActivityThirdBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

val REQUIRED_PERMISSIONS = arrayOf(
    Manifest.permission.CAMERA,
    Manifest.permission.RECORD_AUDIO,
    Manifest.permission.WRITE_EXTERNAL_STORAGE,
    Manifest.permission.READ_EXTERNAL_STORAGE)

const val REQUEST_VIDEO_CAPTURE = 1
const val TAG = "cameraX"
const val FILE_NAME_FORMAT = "yy-MM-dd-HH-mm-ss-SS"
const val REQUEST_CODE_PERMISSIONS = 123

// TO DO -  Create a server to upload videos to (Friday)
//          Have the video created send to the server (Friday)
//          Implement Screens 1 and 2 into this stupid video recording page (Friday)
//          Refactor code (Saturday)
//          Record gesture videos, record demo, turn in, peer review (Saturday)

class ThirdActivity : AppCompatActivity() {

    private lateinit var binding: ActivityThirdBinding
    lateinit var video: VideoView
    private var imageCapture:ImageCapture? = null
    private lateinit var outputDirectory: File
    //private lateinit var cameraExecutor: ExecutorService
    private var vidCapture: VideoCapture? = null
    private var upload: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityThirdBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.buttonRecord.isVisible = true
        binding.buttonUpload.isVisible = false
        outputDirectory = getOutputDirectory()
        //cameraExecutor = Executors.newSingleThreadExecutor()

        if (allPermissionGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        binding.buttonRecord.setOnClickListener {
            dispatchTakeVideoIntent()
            binding.buttonUpload.isVisible = false
        }
        binding.buttonUpload.setOnClickListener {
            //UploadUtility(this).uploadFile(upload)
            val intent = Intent(this@ThirdActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        video = binding.videoView3
        binding.viewFinder.isVisible = false
        var mediaControls: MediaController? = null
        super.onActivityResult(requestCode, resultCode, intent)
        if (mediaControls == null) {
            mediaControls = MediaController(this)
            mediaControls!!.setAnchorView(this.video)
        }
        video!!.setMediaController(mediaControls)
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            val videoUri: Uri? = intent?.data
            upload = intent?.data
            binding.buttonUpload.isVisible = true
            video.setVideoURI(videoUri)
            video!!.requestFocus()
            video!!.start()
        }
    }

    private fun getOutputDirectory(): File{
        val mediaDir = externalMediaDirs.firstOrNull()?.let { mFile->
            File(mFile, "Project1").apply {
                mkdirs()
            }
        }

        return if (mediaDir != null && mediaDir.exists())
            mediaDir else filesDir
    }

    private fun dispatchTakeVideoIntent() {
        val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)

        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 5)
        intent.resolveActivity(packageManager)
        startActivityForResult(intent, REQUEST_VIDEO_CAPTURE)
        /*intent.also {  takeVideoIntent ->
            takeVideoIntent.putExtra(
                android.provider.MediaStore.EXTRA_DURATION_LIMIT, 5).also {
                takeVideoIntent.resolveActivity(packageManager)?.also {
                    startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE)
                }
            }
        }

         */
    }


    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider
            .getInstance(this)
        cameraProviderFuture.addListener( {

            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder()
                .build()
                .also {
                        mpreview->
                    mpreview.setSurfaceProvider(
                        binding.viewFinder.surfaceProvider
                    )
                }
            imageCapture = ImageCapture.Builder()
                .build()

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()

                cameraProvider.bindToLifecycle(
                    this, cameraSelector,
                    preview, imageCapture
                )

            } catch (e:Exception) {
                Log.d(TAG,"startCamera Fail:", e)
            }
        }, ContextCompat.getMainExecutor(this))

    }
    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode == REQUEST_CODE_PERMISSIONS) {
            if(allPermissionGranted()) {
                startCamera()
            } else {
                TODO()
                finish()
            }
        }
    }
    private fun allPermissionGranted() =
        REQUIRED_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(
                baseContext, it
            ) == PackageManager.PERMISSION_GRANTED
        }
}
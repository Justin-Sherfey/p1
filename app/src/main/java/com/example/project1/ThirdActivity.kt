package com.example.project1

import android.Manifest
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.FocusFinder
import android.view.TextureView
import android.widget.Button
import android.widget.FrameLayout
import android.widget.MediaController
import android.widget.VideoView
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.core.VideoCapture
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.common.util.concurrent.ListenableFuture

//https://heartbeat.fritz.ai/uploading-images-from-android-to-a-python-based-flask-server-691e4092a95e
//https://developer.android.com/training/camerax/preview
//https://developer.android.com/guide/topics/media/camera
//https://placona.co.uk/building-a-video-recording-application-in-android-with-camerax/
private const val REQUEST_CODE_PERMISSIONS = 10
private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
private val tag = MainActivity::class.java.simpleName

class ThirdActivity : AppCompatActivity() {

    lateinit var video: VideoView
    var upload: Uri? = null

    private val REQUEST_VIDEO_CAPTURE = 1
    private fun dispatchTakeVideoIntent() {
        val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        intent.putExtra("android.intent.extra.durationLimit", 5)
        intent.also {  takeVideoIntent ->
            takeVideoIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE)
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        video = findViewById(R.id.videoView3)
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
            video.setVideoURI(videoUri)
            video!!.requestFocus()
            video!!.start()
        }
    }


    private lateinit var captureButton: Button
    private lateinit var videoCapture: VideoCapture

    private lateinit var cameraProviderFuture : ListenableFuture<ProcessCameraProvider>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third)

        cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        video = findViewById(R.id.videoView3)
        captureButton = findViewById(R.id.button)

        // button that will bring to third screen with recording
        val thirdPageButton = findViewById<Button>(R.id.button4)
        thirdPageButton.setOnClickListener {

            val intent = Intent(this@ThirdActivity, MainActivity::class.java)
            startActivity(intent)
        }
        captureButton.setOnClickListener {
            dispatchTakeVideoIntent()
        }
    }
}
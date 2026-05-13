package com.example.camerax

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.FileOutputOptions
import androidx.camera.video.PendingRecording
import androidx.camera.video.Recorder
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
import androidx.camera.video.VideoRecordEvent
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private lateinit var viewFinder: PreviewView
    private lateinit var btnCapture: Button
    private lateinit var btnRecord: Button
    private lateinit var btnPause: Button
    private lateinit var ivPreview: ImageView

    private var imageCapture: ImageCapture? = null
    private var videoCapture: VideoCapture<Recorder>? = null
    private var recording: Recording? = null
    private var pendingRecording: PendingRecording? = null
    private var isRecordingPaused = false

    private lateinit var cameraExecutor: ExecutorService

    private val REQUEST_CODE_PERMISSIONS = 10
    private val REQUIRED_PERMISSIONS = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.RECORD_AUDIO
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        setupClickListeners()

        if (allPermissionsGranted()) {
            startCamera()
        } else {
            requestPermissions()
        }
    }

    private fun initViews() {
        viewFinder = findViewById(R.id.viewFinder)
        btnCapture = findViewById(R.id.btnCapture)
        btnRecord = findViewById(R.id.btnRecord)
        btnPause = findViewById(R.id.btnPause)
        ivPreview = findViewById(R.id.ivPreview)

        btnPause.visibility = View.GONE
    }

    private fun setupClickListeners() {
        btnCapture.setOnClickListener { takePhoto() }
        btnRecord.setOnClickListener { captureVideo() }
        btnPause.setOnClickListener { togglePauseRecording() }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                finish()
            }
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder()
                .build()
                .also { it.setSurfaceProvider(viewFinder.surfaceProvider) }

            imageCapture = ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build()

            val recorder = Recorder.Builder().build()
            videoCapture = VideoCapture.withOutput(recorder)

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture, videoCapture
                )
            } catch (exc: Exception) {
                Log.e(TAG, "Camera binding failed", exc)
            }
        }, ContextCompat.getMainExecutor(this))

        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return

        val photoFile = createImageFile()
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val savedUri = Uri.fromFile(photoFile)
                    Log.d(TAG, "Photo saved: $savedUri")
                    showPreviewImage(photoFile)
                }
            }
        )
    }

    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat(FILENAME_FORMAT, Locale.CHINA).format(System.currentTimeMillis())
        return File(externalMediaDirs.firstOrNull(), "${timeStamp}.jpg")
    }

    private fun showPreviewImage(file: File) {
        val bitmap = BitmapFactory.decodeFile(file.absolutePath)
        ivPreview.setImageBitmap(bitmap)
        ivPreview.visibility = View.VISIBLE
    }

    private fun captureVideo() {
        val videoCapture = this.videoCapture ?: return

        val currentRecording = recording
        if (currentRecording != null) {
            stopRecording()
            return
        }

        startRecording(videoCapture)
    }

    private fun startRecording(videoCapture: VideoCapture<Recorder>) {
        val videoFile = createVideoFile()
        val outputOptions = FileOutputOptions.Builder(videoFile).build()

        pendingRecording = videoCapture.output.prepareRecording(this, outputOptions)

        recording = pendingRecording?.start(ContextCompat.getMainExecutor(this)) { event ->
            when (event) {
                is VideoRecordEvent.Start -> {
                    btnRecord.text = "停止"
                    btnRecord.setBackgroundResource(R.drawable.stop_button)
                    btnPause.visibility = View.VISIBLE
                    btnCapture.isEnabled = false
                }
                is VideoRecordEvent.Finalize -> {
                    handleRecordingFinalize(event)
                }
            }
        }
    }

    private fun createVideoFile(): File {
        val timeStamp = SimpleDateFormat(FILENAME_FORMAT, Locale.CHINA).format(System.currentTimeMillis())
        return File(externalMediaDirs.firstOrNull(), "${timeStamp}.mp4")
    }

    private fun handleRecordingFinalize(event: VideoRecordEvent.Finalize) {
        recording = null
        pendingRecording = null
        btnRecord.isEnabled = true
        btnRecord.text = "录像"
        btnRecord.setBackgroundResource(R.drawable.record_button)
        btnPause.visibility = View.GONE
        btnCapture.isEnabled = true
        isRecordingPaused = false

        if (event.hasError()) {
            Log.e(TAG, "Video capture failed: ${event.error}")
        } else {
            Log.d(TAG, "Video saved: ${event.outputResults.outputUri}")
        }
    }

    private fun stopRecording() {
        recording?.stop()
        recording = null
        pendingRecording = null
        btnRecord.isEnabled = true
        btnRecord.text = "录像"
        btnRecord.setBackgroundResource(R.drawable.record_button)
        btnPause.visibility = View.GONE
        btnCapture.isEnabled = true
        isRecordingPaused = false
    }

    private fun togglePauseRecording() {
        recording ?: return

        if (isRecordingPaused) {
            recording?.resume()
            btnPause.text = "暂停"
            isRecordingPaused = false
        } else {
            recording?.pause()
            btnPause.text = "继续"
            isRecordingPaused = true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    companion object {
        private const val TAG = "CameraXApp"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
    }
}
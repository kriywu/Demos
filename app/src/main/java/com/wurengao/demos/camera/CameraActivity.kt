package com.wurengao.demos.camera

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageFormat
import android.graphics.Matrix
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CaptureRequest
import android.media.ImageReader
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.util.Log
import android.util.Size
import android.view.SurfaceView
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.wurengao.demos.R

class CameraActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "CameraActivity"
        private const val CAMERA_PERMISSION_CODE = 100
    }

    private var isResumeFromBackground = false
    private lateinit var cameraDevice: CameraDevice

    private val surfaceView: SurfaceView by lazy { findViewById(R.id.surfaceView) }
    private val target01: ImageView by lazy { findViewById(R.id.target0) }
    private val cameraManager: CameraManager by lazy { getSystemService(Context.CAMERA_SERVICE) as CameraManager }
    private val backgroundThread: HandlerThread by lazy { HandlerThread("CameraBackground") }
    private val backgroundHandler: Handler by lazy {
        backgroundThread.start()
        Handler(backgroundThread.looper)
    }
    private val takePhotoButton: Button by lazy { findViewById(R.id.take_photo) }

    private val defaultWidth by lazy { 1920 * 2 }
    private val defaultHeight by lazy { 1080 * 2 }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

//        surfaceView.layoutParams.apply {
//            width = defaultWidth
//            height = defaultHeight
//        }

        checkAndOpenCamera()
    }

    private fun checkAndOpenCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            openCamera()
            return
        }

        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE)
    }

    private fun onCameraOpened(camera: CameraDevice) {
        takePhotoButton.setOnClickListener {

        }
    }

    private val cameraStateCallback :CameraDevice.StateCallback by lazy {
        object : CameraDevice.StateCallback() {
            override fun onOpened(camera: CameraDevice) {
                cameraDevice = camera
                createCameraPreviewSession()
                onCameraOpened(camera)
                Log.d(TAG, "onOpened: ")
            }

            override fun onDisconnected(camera: CameraDevice) {
                cameraDevice.close()
                Log.d(TAG, "onDisconnected: ")
            }

            override fun onError(camera: CameraDevice, error: Int) {
                cameraDevice.close()
                Log.d(TAG, "onError: $error")
//                this@CameraActivity.finish()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun openCamera() {
        val cameraId = cameraManager.cameraIdList[0]
        val map = cameraManager.getCameraCharacteristics(cameraId).get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
        Log.d(TAG, "openCamera: $map")

        cameraManager.openCamera(cameraId, cameraStateCallback, null)
    }

    private val imageReader by lazy {
        ImageReader.newInstance(defaultWidth, defaultHeight, ImageFormat.JPEG, 1).apply {
            setOnImageAvailableListener({
//                Log.d(TAG, "imageReader: ")
//                val image = it.acquireLatestImage()
//                val buffer = image.planes[0].buffer
//                val imageBytes = ByteArray(buffer.remaining())
//                buffer.get(imageBytes)
//                image.close()
//
//                // 将字节数据转换为Bitmap
//                val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
//
//                // 处理Bitmap，例如保存或显示
//                val exifOrientation = getExifOrientation(imageBytes)

//                imageRender.setImageBitmap(rotateBitmap(bitmap, 90))
//                bitmap.recycle()
            }, Handler(Looper.getMainLooper()))
        }
    }

    // 旋转Bitmap的函数
    fun rotateBitmap(bitmap: Bitmap, orientation: Int): Bitmap {
        val matrix = Matrix()
        when (orientation) {
            90 -> matrix.postRotate(90F)
            180 -> matrix.postRotate(180F)
            270 -> matrix.postRotate(270F)
            // 其他角度的处理...
        }
        val rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        bitmap.recycle() // 由于创建了新的旋转后的Bitmap，原始Bitmap可以被回收
        return rotatedBitmap
    }

    private fun createCameraPreviewSession() {
        val cameraCaptureSessionCallback = object : CameraCaptureSession.StateCallback() {
            override fun onConfigured(session: CameraCaptureSession) {
                val captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE)
                captureRequestBuilder.addTarget(surfaceView.holder.surface) // Surface 预览
//                captureRequestBuilder.addTarget(imageReader.surface)
//                captureRequestBuilder.addTarget(surfaceTextureSurface)      // 拍照

//                captureRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_AUTO);
                captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CaptureRequest.CONTROL_MODE_AUTO);
                captureRequestBuilder.set(CaptureRequest.JPEG_THUMBNAIL_SIZE, Size(defaultWidth, defaultHeight));
                session.setRepeatingRequest(captureRequestBuilder.build(), XGCaptureCallback(), backgroundHandler)
            }

            override fun onConfigureFailed(session: CameraCaptureSession) {
                Log.e(TAG, "Camera configuration failed.")
            }
        }

        Log.d(
            TAG,
            "createCameraPreviewSession: ${surfaceView.holder.surfaceFrame.width()} ${surfaceView.holder.surfaceFrame.height()}"
        )

        cameraDevice.createCaptureSession(listOf(
            surfaceView.holder.surface,
//            imageReader.surface
        ), cameraCaptureSessionCallback, backgroundHandler)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera()
            } else {
                Toast.makeText(this, "Camera permission denied.", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (isResumeFromBackground) {
            openCamera()
            isResumeFromBackground = false
        }
    }

    override fun onPause() {
        super.onPause()
        isResumeFromBackground = true
        cameraDevice.close()
    }
}

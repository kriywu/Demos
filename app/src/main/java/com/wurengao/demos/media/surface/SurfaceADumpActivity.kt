package com.wurengao.demos.media.surface

import android.annotation.TargetApi
import android.content.res.AssetFileDescriptor
import android.graphics.Outline
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.wurengao.demos.R

class SurfaceADumpActivity : AppCompatActivity(), SurfaceHolder.Callback {

    private lateinit var surfaceView: SurfaceView
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var imageView: ImageView
//    private lateinit var container: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_surface_adump)

        surfaceView = findViewById(R.id.surface_view)
        surfaceView.holder.addCallback(this)
        imageView = findViewById(R.id.iv)
//        container = findViewById(R.id.container)
//
//        container.outlineProvider = VideoRoundCornerViewOutlineProvider(64)
//        container.clipToOutline = true

        imageView.setOnClickListener {
//            val bitmap = Bitmap.createBitmap(container.width, container.height, Bitmap.Config.ARGB_4444)
//            container.draw(Canvas(bitmap))
//            imageView.setImageBitmap(bitmap)

            val layout = imageView.layoutParams
            layout.height = layout.height * 2
            imageView.layoutParams = layout
        }
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        mediaPlayer = MediaPlayer()
        try {
            val afd: AssetFileDescriptor = resources.openRawResourceFd(R.raw.video2)
            mediaPlayer.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
            mediaPlayer.setDisplay(holder)
            mediaPlayer.prepare()
            mediaPlayer.start()
            mediaPlayer.setOnCompletionListener {
                mediaPlayer.start()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        // Handle surface changes
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        mediaPlayer.release()
    }
}

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
class VideoRoundCornerViewOutlineProvider(private val mRadius: Int) : ViewOutlineProvider() {
    override fun getOutline(view: View, outline: Outline) {
        outline.setRoundRect(0, 0, view.width, view.height, mRadius.toFloat())
    }
}
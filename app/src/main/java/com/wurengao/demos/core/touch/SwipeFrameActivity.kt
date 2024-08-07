package com.wurengao.demos.core.touch

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.wurengao.demos.R


class SwipeFrameLayout constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : FrameLayout(context, attrs) {

    private var downX = 0f
    private var downY = 0f
    var move: ((Float) -> Unit)? = null;
    var up: ((Boolean) -> Unit)? = null
    var down: (() -> Unit)? = null

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        Log.d("WRG", "dispatchTouchEvent: ${ev.action}")
        when(ev.action) {
            MotionEvent.ACTION_DOWN -> {
                downX = ev.rawX
                downY = ev.rawY
                down?.invoke()
            }

            MotionEvent.ACTION_MOVE -> {
                val deltaY = ev.rawY - downY
                move?.invoke(deltaY)
            }

            MotionEvent.ACTION_UP -> {
                val deltaY = ev.rawY - downY
                up?.invoke(deltaY > 400)
            }
        }
        return true
    }


}
class GestureActivity : AppCompatActivity() {


    private val feed by lazy { findViewById<FrameLayout>(R.id.feed) }
    private val camera by lazy { findViewById<FrameLayout>(R.id.camera) }
    private val gesture by lazy { findViewById<SwipeFrameLayout>(R.id.gesture) }

    private var isOpen = false
    private val defaultTranslationY by lazy { - Utils.getScreenHeight(this).toFloat()  }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_swipe_frame)

        gesture.move = {
            if (isOpen) {
                if (it < 0)
                    camera.translationY = 0 + it
            } else {
                camera.translationY = defaultTranslationY + it
            }
        }

        gesture.up = {
            if (it) {
                camera.translationY = 0f
                isOpen = true
            } else {
                camera.translationY = defaultTranslationY
                camera.visibility = View.GONE
                isOpen = false
            }
        }

        gesture.down = {
            if (camera.visibility == View.VISIBLE) {

            } else {
                camera.visibility = View.VISIBLE
                camera.translationY = defaultTranslationY
            }
        }
    }
}

object Utils {
    fun getScreenHeight(context: Context): Int {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val screenHeight: Int

        // 使用不同的API级别获取屏幕高度
        screenHeight = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            val metrics = context.resources.displayMetrics
            metrics.heightPixels
        } else {
            @Suppress("DEPRECATION")
            val size = android.graphics.Point()
            display.getSize(size)
            size.y
        }

        return screenHeight
    }
}
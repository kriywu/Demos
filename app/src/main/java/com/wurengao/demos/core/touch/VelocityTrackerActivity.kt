package com.wurengao.demos.core.touch

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.wurengao.demos.R


class MyVelocityTrackerView(context: Context): ImageView(context) {
    companion object {
        private const val TAG = "VelocityTrackerActivity"
    }
    private val velocityTracker: VelocityTracker by lazy { VelocityTracker.obtain() }

    init {
        setOnClickListener {
            Log.d(TAG, "onClick")

        }
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        Log.d(TAG, "dispatchTouchEvent: ${event?.action}")
        return super.dispatchTouchEvent(event)
    }

    override fun performClick(): Boolean {
        Log.d(TAG, "performClick: ")
        return super.performClick()
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Log.d(TAG, "onTouchEvent: ${event?.action}")
        event ?: return super.onTouchEvent(event)

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                velocityTracker.clear()
                velocityTracker.addMovement(event)
            }

            MotionEvent.ACTION_MOVE -> {
                velocityTracker.addMovement(event)
            }

            MotionEvent.ACTION_UP -> {
                velocityTracker.computeCurrentVelocity(1000)
                val x = velocityTracker.xVelocity
                val y = velocityTracker.yVelocity
                Log.d(TAG, "onTouchEvent: Vx=$x Vy=$y")
            }
        }


        val ret = super.onTouchEvent(event)
        Log.d(TAG, "onTouchEvent: $ret")
        return ret
    }


}

class VelocityTrackerActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_velocity_tracker)

        val group = findViewById<LinearLayout>(R.id.container)
        val child = MyVelocityTrackerView(this)
        child.setBackgroundColor(resources.getColor(R.color.black))
        child.setImageResource(R.drawable.ic_launcher_background)

        group.addView(child, ViewGroup.LayoutParams(100, 100))

        group.setOnClickListener {
            Log.d("", "onclick: ")
        }

        group.setOnTouchListener { v, event ->
            child.scrollTo(event.x.toInt(), event.y.toInt())
            child.translationX = event.rawX
            child.translationY = event.rawY
            return@setOnTouchListener true
        }

    }
}
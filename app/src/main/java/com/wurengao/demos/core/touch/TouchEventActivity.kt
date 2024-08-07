package com.wurengao.demos.core.touch

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.wurengao.demos.R

class MyView(context: Context?) : View(context) {
    companion object {
        private const val TAG = "TouchEventActivity"
    }

    init {
        setOnClickListener {
            Log.d(TAG, "on click: ")
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
        Log.d(TAG, "onTouchEvent: ")
        return super.onTouchEvent(event)
    }
}

class TouchEventActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_touch_event)


        val container = findViewById<LinearLayout>(R.id.container)

        val childView = MyView(this)
        childView.setBackgroundColor(resources.getColor(R.color.black))
        container.addView(childView, ViewGroup.LayoutParams(800, 1000))
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        val ret = super.onCreateView(name, context, attrs)
        return ret
    }


}
package com.wurengao.demos.view_basic

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.wurengao.demos.R


private const val TAG = "TouchEventDispatchActiv"

class MyLinearLayout: LinearLayout {

    constructor(context: Context): super(context)

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) ;


    fun getEntryName(): String {
        return resources.getResourceEntryName(getId())

    }

//    init {
//        setOnClickListener {
//            Log.d(TAG, "${getEntryName()} onClick")
//        }
//    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        Log.d(TAG, "${getEntryName()} dispatchTouchEvent start: ${ev?.action}")
        val ret = super.dispatchTouchEvent(ev)
        Log.d(TAG, "${getEntryName()} dispatchTouchEvent end: ${ev?.action} $ret")
        return ret
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        Log.d(TAG, "${getEntryName()} onInterceptTouchEvent start: ${ev?.action}")
        val ret = super.onInterceptTouchEvent(ev)
        Log.d(TAG, "${getEntryName()} onInterceptTouchEvent end: ${ev?.action} $ret")
        return ret
    }

    override fun performClick(): Boolean {
        Log.d(TAG, "${getEntryName()} performClick start:")
        val ret = super.performClick()
        Log.d(TAG, "${getEntryName()} performClick end: $ret")
        return ret
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Log.d(TAG, "${getEntryName()} onTouchEvent start ${event?.action}")
        val ret = super.onTouchEvent(event)
        Log.d(TAG, "${getEntryName()} onTouchEvent end ${event?.action} ret=$ret")
        return ret
    }
}


class MyButton: Button {

    constructor(context: Context): super(context)

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) ;

    init {
//        setOnClickListener {
//            Log.d(TAG, "${getEntryName()} onClick")
//        }
    }

    fun getEntryName(): String {
        return resources.getResourceEntryName(getId())

    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        Log.d(TAG, "${getEntryName()} dispatchTouchEvent start: ${ev?.action}")
        val ret = super.dispatchTouchEvent(ev)
        Log.d(TAG, "${getEntryName()} dispatchTouchEvent end: ${ev?.action} $ret")
        return ret
    }

    override fun performClick(): Boolean {
        Log.d(TAG, "${getEntryName()} performClick start:")
        val ret = super.performClick()
        Log.d(TAG, "${getEntryName()} performClick end: $ret")
        return ret
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Log.d(TAG, "${getEntryName()} onTouchEvent start ${event?.action}")
        val ret = super.onTouchEvent(event)
        Log.d(TAG, "${getEntryName()} onTouchEvent end ${event?.action} ret=$ret")
        return ret
    }
}

class TouchEventDispatchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_touch_event_dispatch)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Log.d(TAG, "activity onTouchEvent start ${event?.action}")
        val ret = super.onTouchEvent(event)
        Log.d(TAG, "activity onTouchEvent end ${event?.action} ret=$ret")
        return ret
    }
}
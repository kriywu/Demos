package com.wurengao.demos.scroll_conflict

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.Log
import android.view.MotionEvent
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.ScrollView
import androidx.appcompat.app.AppCompatActivity
import com.wurengao.demos.R
import kotlin.math.abs

private const val TAG = "ScrollConflictActivity"

class MyScrollView: ScrollView {
    private var downX = 0f
    private var downY = 0f

    private var lastX = 0f
    private var lastY = 0f

    constructor(context: Context) : super(context) {

    }

    constructor(context: Context, attributeSet: AttributeSet): super(context, attributeSet) {

    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        ev ?: return true
        val ret = super.dispatchTouchEvent(ev)
        lastX = ev.rawX
        lastY = ev.rawY
        return ret
    }


    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        ev ?: return true
        var intercept = false

        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                intercept = false
                downX = ev.rawX
                downY = ev.rawY
            }

            MotionEvent.ACTION_MOVE -> {
                val deltaX = ev.rawX - lastX
                val deltaY = ev.rawY - lastY
                intercept = abs(deltaY) < abs(deltaX)
            }

            MotionEvent.ACTION_UP -> {
                intercept = false
            }
        }

        return intercept
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        ev ?: return true
        when(ev.action) {
            MotionEvent.ACTION_DOWN -> {}

            MotionEvent.ACTION_MOVE -> {
                val deltaX = lastX - ev.rawX
                val deltaY = lastY - ev.rawY
                scrollBy(deltaX.toInt(), deltaY.toInt())
            }

            MotionEvent.ACTION_UP -> {}
        }

        return super.onTouchEvent(ev)
    }
}


fun getScreenWidth(context: Context): Int {
    val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val metrics = DisplayMetrics()
    windowManager.defaultDisplay.getMetrics(metrics)
    return metrics.widthPixels
}

class ScrollConflictActivity : AppCompatActivity() {

    val data = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scroll_conflict)

        val scrollView = findViewById<ScrollView>(R.id.scrollView)
        val listContainer = findViewById<LinearLayout>(R.id.listContainer)
        listContainer.post {
            Log.d(TAG, "onCreate: ${listContainer.width}")
            listContainer.layoutParams.width = 2 * getScreenWidth(this)
            listContainer.requestLayout()
            Log.d(TAG, "onCreate: ${listContainer.layoutParams.width}")
        }

        val listView1 = findViewById<ListView>(R.id.listView)
        val listView2 = findViewById<ListView>(R.id.listView2)

        listView1.adapter = ArrayAdapter(this, R.layout.list_view_item, data)
        listView2.adapter = ArrayAdapter(this, R.layout.list_view_item, data)

        listView1.post {
            listView1.layoutParams.width = getScreenWidth(this)
            listView1.requestLayout()
        }

        listView2.post {
            listView2.layoutParams.width = getScreenWidth(this)
            listView2.requestLayout()
        }

        val intent = Intent()
        intent.action = ""
        intent.categories.add("")
        intent.categories.add("")
        intent.data = Uri.EMPTY
    }
}
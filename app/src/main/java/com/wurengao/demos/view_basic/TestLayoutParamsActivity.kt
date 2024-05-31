package com.wurengao.demos.view_basic

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.wurengao.demos.R

class TestLayoutParamsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_layout_params)

        val view = findViewById<View>(R.id.myview)
        view.setOnClickListener {
            val layout = view.layoutParams as? ViewGroup.MarginLayoutParams
            layout ?: return@setOnClickListener

            out(layout.width)
            out(layout.height)
            out(layout.leftMargin)
            out(layout.topMargin)
            out(layout.rightMargin)
            out(layout.bottomMargin)
            out(layout.marginStart) // 优先级更高
            out(layout.marginEnd)   // 优先级更高

            out(view.top) // 左上角坐标
            out(view.bottom) // 右下角坐标
            out(view.left)
            out(view.right)
            out(view.x) // left + translationX
            out(view.y) // top + translationY

            view.width // right - left
            view.height // bottom - top

            view.translationX  // 渲染偏移量
            view.translationY  // 渲染偏移量
        }

    }


    private fun out(msg: Any?) {
        msg ?: return
        val call = Thread.currentThread().stackTrace[3]
        Log.d("HH", "${call.lineNumber} out: $msg")
    }
}
package com.wurengao.surfaceviewtestdemo.launch_mode

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.wurengao.surfaceviewtestdemo.R

class FirstActivity : AppCompatActivity() {
    val btn by lazy { findViewById<Button>(R.id.btn1) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first)

        btn.setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java)
            startActivity(intent)
        }

    }
}
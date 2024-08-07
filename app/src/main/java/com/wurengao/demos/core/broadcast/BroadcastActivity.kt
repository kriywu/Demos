package com.wurengao.demos.core.broadcast

import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.wurengao.demos.R

class BroadcastActivity : AppCompatActivity() {

    private var receiver: BroadcastReceiver? = null

    private val btn by lazy { findViewById<Button>(R.id.send) }
    private val action =  "com.wurengao.demos.MY_BROADCAST"
    private var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_broadcaset)
        val intentFilter = IntentFilter()
        intentFilter.addAction(action)
        receiver = MyReceiver()
        registerReceiver(receiver, intentFilter, null, null)

        btn.setOnClickListener {
            val intent = Intent(action)
            intent.putExtra("msg", "${count++}")
            sendBroadcast(intent)
            sendOrderedBroadcast(intent, null)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }
}
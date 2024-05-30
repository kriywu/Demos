package com.wurengao.surfaceviewtestdemo.mmap

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.wurengao.surfaceviewtestdemo.R
import com.wurengao.surfaceviewtestdemo.utils.NativeUtils.nativeReadFromMMap
import com.wurengao.surfaceviewtestdemo.utils.NativeUtils.nativeWriteToMMap

class MMapDemoActivity : AppCompatActivity() {

    private val etInput by lazy { findViewById<EditText>(R.id.input) }
    private val etOutput by lazy { findViewById<EditText>(R.id.output) }
    private val btnWrite by lazy { findViewById<Button>(R.id.writeToMMap) }
    private val btnRead by lazy { findViewById<Button>(R.id.readFromMMap) }

    private val inputString = MutableLiveData<String>("")
    private val outputString = MutableLiveData<String>("")

    companion object {
        private const val TAG = "MMapDemoActivity"
        private const val MMAP_PATH = "/sdcard/Android/data/com.wurengao.surfaceviewtestdemo/files/cache/mmap"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mmap_demo)

        btnWrite.setOnClickListener {
            inputString.value = etInput.text.toString()
        }

        btnRead.setOnClickListener {
            outputString.value = nativeReadFromMMap(MMAP_PATH)
        }

        inputString.observe(this) {
            val ret = nativeWriteToMMap(MMAP_PATH, it)
            Toast.makeText(this, "write mmap return=$ret", Toast.LENGTH_SHORT).show()
        }

        outputString.observe(this) {
            etOutput.setText(it)
        }

    }
}
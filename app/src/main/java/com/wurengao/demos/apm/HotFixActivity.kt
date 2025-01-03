package com.wurengao.demos.apm

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.wurengao.demos.R

class HotFixActivity : AppCompatActivity() {

    private val TAG = "HotFixActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerUnCatchException()
        setContentView(R.layout.activity_hot_fix)
        method1()
    }

    fun registerUnCatchException() {
        Thread.setDefaultUncaughtExceptionHandler() { t, e ->
            if (e is NullPointerException) {
                Log.w(TAG, "caught exception thread=$t e=$e")
                return@setDefaultUncaughtExceptionHandler
            }
            Log.w(TAG, "uncaught exception thread=$t e=$e")
        }
    }


    fun method1() {
//      thread { npe() } // caught OK
        Handler(mainLooper).postDelayed({ npe() }, 1000 ) // caught OK
//      npe() // caught OK but ANR
    }

    fun npe() {
        val o: Object? = null
        o!!.notify()
    }
}
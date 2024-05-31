package com.wurengao.demos.reentrantlock

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.wurengao.demos.R
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.thread

class ReentrantLockTestActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "ReentrantLockTestActivi"
    }

    val conditionOneButton by lazy { findViewById<Button>(R.id.btn1) }
    val conditionTwoButton by lazy { findViewById<Button>(R.id.btn2) }
    val tryLockButton by lazy { findViewById<Button>(R.id.btn3) }
    val console by lazy { findViewById<TextView>(R.id.out) }

    val consoleData :MutableLiveData<String> = MutableLiveData<String>("")

    val lock: Lock = ReentrantLock()
    val condition1 = lock.newCondition()
    val condition2 = lock.newCondition()

    var th1: Thread? = null
    var th2: Thread? = null
    var th3: Thread? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reentrant_lock_test)

        th1 = thread {
            Log.d(TAG, "before lock")
            lock.lock()
            Log.d(TAG, "lock, await")
            condition1.await()
            lock.unlock()
            Log.d(TAG, "lock, notify")
            conditionOneButton.post {
                conditionOneButton.text =  conditionOneButton.text.toString() + "✅"
            }
        }

        th2 = thread {
            Log.d(TAG, "before lock")
            lock.lock()
            Log.d(TAG, "lock, await")
            condition2.await()
            Log.d(TAG, "lock, notify")
            lock.unlock()
            conditionTwoButton.post {
                conditionTwoButton.text = conditionTwoButton.text.toString() + "✅"
            }
        }

        th3 = thread {
            lock.lock()
            condition1.await()
            condition2.await()
            lock.unlock()
            console.post {
                consoleData.value = "all condition done ✅"
            }
        }

        conditionOneButton.setOnClickListener {
            lock.lock()
            condition1.signalAll()
            lock.unlock()
        }

        conditionTwoButton.setOnClickListener {
            lock.lock()
            condition2.signalAll(  )
            lock.unlock()
        }

        consoleData.observe(this) { console.text = it }
    }
}
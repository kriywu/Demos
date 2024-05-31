package com.wurengao.surfaceviewtestdemo.binder

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.wurengao.surfaceviewtestdemo.R


class BinderClientActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "Binder"
        private const val uid = "wrg"
        private const val pwd = "admin"
    }

    private val btnSignUp by lazy { findViewById<Button>(R.id.signUp) }
    private val btnSignIn by lazy { findViewById<Button>(R.id.signIn) }
    private val btnGetAccountInfo by lazy { findViewById<Button>(R.id.getAccountInfo) }
    private var remoteInterface :IAccountManagerInterface? = null

    private val conn = object: ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            remoteInterface = IAccountManagerInterface.asInterface(service)
            Log.d(TAG, "onServiceConnected: ")
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            remoteInterface = null
            Log.d(TAG, "onServiceDisconnected: ")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(conn)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_binder_client)
        val intent = Intent(this, AccountService::class.java)
        intent.component = ComponentName(packageName, AccountService::class.qualifiedName!!)
        bindService(intent, conn, BIND_AUTO_CREATE)

        btnSignUp.setOnClickListener {
            val ret = remoteInterface?.signUp(uid, pwd)
            Log.d(TAG, "btnSignUp: $ret")
        }

        btnSignIn.setOnClickListener {
            val ret = remoteInterface?.signIn(uid, pwd)
            Log.d(TAG, "btnSignIn: $ret")
        }

        btnGetAccountInfo.setOnClickListener {
            val ret = remoteInterface?.getAccountInfo(uid)
            val account = Account.fromJSONString(ret!!)
            Log.d(TAG, "btnGetAccountInfo: $account")
        }
    }
}
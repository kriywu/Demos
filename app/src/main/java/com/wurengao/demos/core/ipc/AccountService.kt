package com.wurengao.demos.core.ipc

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

class AccountService : Service() {

    companion object {
        private const val TAG = "Binder"
    }

    private var uid: String? = null
    private var pwd: String? = null

    private val stub = object : IAccountManagerAidlInterfaceStub() {
        override fun signUp(uid: String?, pwd: String?): Int {
            Log.d(TAG, "signUp: cpid=${getCallingPid()} pid=${android.os.Process.myPid()}")
            this@AccountService.uid = uid
            this@AccountService.pwd = pwd
            return 0
        }

        override fun signIn(uid: String?, pwd: String?): Int {
            Log.d(TAG, "signIn: cpid=${getCallingPid()} pid=${android.os.Process.myPid()}")
            val success = this@AccountService.uid == uid && this@AccountService.pwd == pwd
            return if (success) 0 else -1
        }

        override fun getAccountInfo(uid: String?): String {
            Log.d(TAG, "getAccountInfo: cpid=${getCallingPid()} pid=${android.os.Process.myPid()}")
            return Account(uid.toString(), pwd.toString()).toString()
        }

    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate: ")
    }

    override fun onBind(intent: Intent): IBinder {
        Log.d(TAG, "onBind: ")
        return stub
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.d(TAG, "onUnbind: ")
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy: ")
        super.onDestroy()
    }
}
package com.wurengao.surfaceviewtestdemo.binder

import android.os.Binder
import android.os.IBinder
import android.os.IInterface
import android.os.Parcel
import android.os.RemoteException
import com.wurengao.surfaceviewtestdemo.binder.IAccountManagerInterface.Companion.TRANSACTION_getAccountInfo
import com.wurengao.surfaceviewtestdemo.binder.IAccountManagerInterface.Companion.TRANSACTION_signIn
import com.wurengao.surfaceviewtestdemo.binder.IAccountManagerInterface.Companion.TRANSACTION_signUp


// 1. 服务接口
// 2. Proxy 服务端代理类，封装BPBinder提供给客户端使用。（调用方）
// 3. Stub 客户端代理类，封装BBinder提供给服务端使用，并且定义好了接口，服务端实现即可。类似于一个HttpServer。（服务方）
// Proxy call Stub by binder-driver to cross process communication
interface IAccountManagerInterface : IInterface {
    companion object {
        const val interfaceDescriptor = "com.wurengao.surfaceviewtestdemo.binder.IAccountManagerInterface"
        const val TRANSACTION_signUp = Binder.FIRST_CALL_TRANSACTION + 0
        const val TRANSACTION_signIn = Binder.FIRST_CALL_TRANSACTION + 1
        const val TRANSACTION_getAccountInfo = Binder.FIRST_CALL_TRANSACTION + 2

        // 获取时机的服务端句柄，提供给客户端使用
        fun asInterface(obj: IBinder?): IAccountManagerInterface? {
            if (obj == null) return null

            val iin = obj.queryLocalInterface(interfaceDescriptor)
            if (iin != null && iin is IAccountManagerInterface) {
                return iin
            }
            return Proxy(obj)
        }
    }

    @Throws(RemoteException::class)
    fun signUp(uid: String?, pwd: String?): Int

    @Throws(RemoteException::class)
    fun signIn(uid: String?, pwd: String?): Int

    @Throws(RemoteException::class)
    fun getAccountInfo(uid: String?): String?
}

// 提供给服务端使用，类似于WebServer框架
// 定义一个 Stub 封装BBinder传输能力
// 服务端可以通过继承 Stub 实现服务端能力
abstract class IAccountManagerAidlInterfaceStub : Binder(), IAccountManagerInterface {
    override fun asBinder() = this

    /** Construct the stub at attach it to the interface.  */
    init {
        attachInterface(this, IAccountManagerInterface.interfaceDescriptor)
    }
    
    @Throws(RemoteException::class)
    public override fun onTransact(code: Int, data: Parcel, reply: Parcel?, flags: Int): Boolean {
        val descriptor = IAccountManagerInterface.interfaceDescriptor
        if (code in FIRST_CALL_TRANSACTION..LAST_CALL_TRANSACTION) {
            data.enforceInterface(descriptor)
        }
        when (code) {
            INTERFACE_TRANSACTION -> {
                reply?.writeString(descriptor)
                return true
            }
        }
        when (code) {
            TRANSACTION_signUp -> {
                val ret = signUp(data.readString(),  data.readString())
                reply?.writeNoException()
                reply?.writeInt(ret)
            }

            TRANSACTION_signIn -> {
                val ret = signIn(data.readString(), data.readString())
                reply?.writeNoException()
                reply?.writeInt(ret)
            }

            TRANSACTION_getAccountInfo -> {
                val info = getAccountInfo(data.readString())
                reply?.writeNoException()
                reply?.writeString(info)
            }

            else -> {
                return super.onTransact(code, data, reply, flags)
            }
        }
        return true
    }
}

// mRemote -> BpBinder（提供跨进程通讯的能力）
// Proxy 封装了跨进程通讯的能力，提供给客户端使用
// 发送方
class Proxy(private val mRemote: IBinder) : IAccountManagerInterface {
    override fun asBinder(): IBinder {
        return mRemote
    }
    
    @Throws(RemoteException::class)
    override fun signUp(uid: String?, pwd: String?): Int {
        val data = Parcel.obtain()
        val reply = Parcel.obtain()
        val result: Int = try {
            data.writeInterfaceToken(IAccountManagerInterface.interfaceDescriptor)
            data.writeString(uid)
            data.writeString(pwd)
            mRemote.transact(TRANSACTION_signUp, data, reply, 0)
            reply.readException()
            reply.readInt()
        } finally {
            reply.recycle()
            data.recycle()
        }
        return result
    }

    @Throws(RemoteException::class)
    override fun signIn(uid: String?, pwd: String?): Int {
        val data = Parcel.obtain()
        val reply = Parcel.obtain()
        val result: Int = try {
            data.writeInterfaceToken(IAccountManagerInterface.interfaceDescriptor)
            data.writeString(uid)
            data.writeString(pwd)
            mRemote.transact(TRANSACTION_signIn, data, reply, 0)
            reply.readException()
            reply.readInt()
        } finally {
            reply.recycle()
            data.recycle()
        }
        return result
    }

    @Throws(RemoteException::class)
    override fun getAccountInfo(uid: String?): String? {
        val data = Parcel.obtain()
        val reply = Parcel.obtain()
        val result: String? = try {
            data.writeInterfaceToken(IAccountManagerInterface.interfaceDescriptor)
            data.writeString(uid)
            mRemote.transact(TRANSACTION_getAccountInfo, data, reply, 0)
            reply.readException()
            reply.readString()
        } finally {
            reply.recycle()
            data.recycle()
        }
        return result
    }
}
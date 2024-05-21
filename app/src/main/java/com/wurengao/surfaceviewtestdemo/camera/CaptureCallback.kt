package com.wurengao.surfaceviewtestdemo.camera

import android.graphics.Bitmap
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CaptureFailure
import android.hardware.camera2.CaptureRequest
import android.hardware.camera2.CaptureResult
import android.hardware.camera2.TotalCaptureResult
import android.util.Log
import android.view.Surface


/**
 * Created by wurengao on 2024/5/17
 * @author wurengao@bytedance.com
 */

class XGCaptureCallback: CameraCaptureSession.CaptureCallback() {

    companion object {
        private const val TAG = "CaptureCallback"
    }

    override fun onCaptureStarted(
        session: CameraCaptureSession,
        request: CaptureRequest,
        timestamp: Long,
        frameNumber: Long
    ) {
        super.onCaptureStarted(session, request, timestamp, frameNumber)
        Log.d(TAG, "onCaptureStarted: ")
    }

    override fun onCaptureProgressed(
        session: CameraCaptureSession,
        request: CaptureRequest,
        partialResult: CaptureResult
    ) {
        super.onCaptureProgressed(session, request, partialResult)
        Log.d(TAG, "onCaptureProgressed: ")
    }

    override fun onCaptureCompleted(
        session: CameraCaptureSession,
        request: CaptureRequest,
        result: TotalCaptureResult
    ) {
        super.onCaptureCompleted(session, request, result)
        Log.d(TAG, "onCaptureCompleted: ${result.frameNumber} ${result.partialResults.size}")

    }

    fun processBitmap(bitmap: Bitmap) {
        // 在这里实现Bitmap的处理逻辑，比如保存图片或更新UI
    }

    override fun onCaptureFailed(
        session: CameraCaptureSession,
        request: CaptureRequest,
        failure: CaptureFailure
    ) {
        super.onCaptureFailed(session, request, failure)
        Log.d(TAG, "onCaptureFailed: ")
    }

    override fun onCaptureSequenceCompleted(
        session: CameraCaptureSession,
        sequenceId: Int,
        frameNumber: Long
    ) {
        super.onCaptureSequenceCompleted(session, sequenceId, frameNumber)
        Log.d(TAG, "onCaptureSequenceCompleted: ")
    }

    override fun onCaptureSequenceAborted(
        session: CameraCaptureSession,
        sequenceId: Int
    ) {
        super.onCaptureSequenceAborted(session, sequenceId)
        Log.d(TAG, "onCaptureSequenceAborted: ")
    }


    override fun onCaptureBufferLost(
        session: CameraCaptureSession,
        request: CaptureRequest,
        target: Surface,
        frameNumber: Long
    ) {
        super.onCaptureBufferLost(session, request, target, frameNumber)
        Log.d(TAG, "onCaptureBufferLost: ")
    }
}
package com.wurengao.demos.audio

import android.content.Context
import android.util.Log
import android.widget.Toast
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

/**
 * Created by wurengao on 2024/5/21
 * @author wurengao@bytedance.com
 */

fun copyAssetToSDCard(context: Context, rawFileID: Int, sdCardPath: String?) {
    sdCardPath ?: return
    try {
        val inputStream = context.resources.openRawResource(rawFileID)
        val outputStream: OutputStream = FileOutputStream(sdCardPath) // 默认覆盖文件
        val buffer = ByteArray(1024)
        var length: Int
        while (inputStream.read(buffer).also { length = it } > 0) {
            outputStream.write(buffer, 0, length)
        }
        inputStream.close()
        outputStream.close()
        Toast.makeText(context, "copy success", Toast.LENGTH_LONG).show()

    } catch (e: IOException) {
        e.printStackTrace()
        Log.e("CopyAssetToSDCard", "Error copying file from assets to SD card")
    }
}
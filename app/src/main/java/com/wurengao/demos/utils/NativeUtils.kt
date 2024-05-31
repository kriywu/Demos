package com.wurengao.demos.utils

/**
 * Created by wurengao on 2024/5/20
 * @author wurengao@bytedance.com
 */

interface IProgress {
    fun onProgress(value: Int)
}

object NativeUtils {
    init {
        System.loadLibrary("native-lib")
    }

    external fun transformMp3ToPCM(mp3File: String, pcmFile: String, callback: IProgress): Int

    external fun transformPCMToMp3(mp3File: String, pcmFile: String, callback: IProgress): Int

    external fun nativeReadFromMMap(path: String): String

    external fun nativeWriteToMMap(path: String, content: String): Int

}
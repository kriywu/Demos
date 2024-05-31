package com.wurengao.demos.audio

import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.util.Log
import java.io.FileInputStream
import kotlin.concurrent.thread


object PCMPlayer {
    private const val TAG = "MediaPlayer"

    private val audioTrack: AudioTrack by lazy {
        val minBufferSizeInBytes = AudioTrack.getMinBufferSize(sampleRate, channelConfig, audioFormat)
        bufferSize = Math.max(minBufferSizeInBytes, bufferSize)
        AudioTrack(AudioManager.STREAM_MUSIC, sampleRate, channelConfig, audioFormat, bufferSize, AudioTrack.MODE_STREAM)
    }
    private val sampleRate = 44100 // 采样率
    private val channelConfig = AudioFormat.CHANNEL_OUT_STEREO // 声道配置
    private val audioFormat = AudioFormat.ENCODING_PCM_16BIT // 音频编码格式
    private var bufferSize = 4096 // 缓冲区大小
    private var thread: Thread? = null
    private var isPlaying = false


    fun asyncPlayPCMFile(filePath: String?) {
        if (isPlaying) {
            isPlaying = false
            return
        }
        thread = thread {
            try {
                isPlaying = true
                val fileInputStream = FileInputStream(filePath)
                val buffer = ByteArray(bufferSize)

                audioTrack.play() // 开始播放
                var bytesRead: Int
                while (fileInputStream.read(buffer).also { bytesRead = it } != -1) {
                    if (isPlaying) {
                        audioTrack.write(buffer, 0, bytesRead) // 写入数据
                    } else {
                        break
                    }
                }
            } catch (e: Exception) {
                Log.w(TAG, "asyncPlayPCMFile: ", e)
            } finally {
                audioTrack.stop()
            }
        }
    }


}
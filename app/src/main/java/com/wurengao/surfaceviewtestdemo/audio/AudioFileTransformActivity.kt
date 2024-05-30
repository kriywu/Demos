package com.wurengao.surfaceviewtestdemo.audio

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.wurengao.surfaceviewtestdemo.R
import com.wurengao.surfaceviewtestdemo.utils.IProgress
import com.wurengao.surfaceviewtestdemo.utils.NativeUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File


class AudioFileTransformActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "AudioFileTransformActiv"
    }
    private val etSourceMP3 by lazy { findViewById<EditText>(R.id.et_source_mp3) }
    private val etSourcePCM by lazy { findViewById<EditText>(R.id.et_source_pcm) }
    private val etTargetMP3 by lazy { findViewById<EditText>(R.id.et_target_mp3) }
    private val etTargetPCM by lazy { findViewById<EditText>(R.id.et_target_pcm) }
    private val btnCopy by lazy { findViewById<Button>(R.id.btn_copy_asset_to_sd) }
    private val btnMp3ToPCM by lazy { findViewById<Button>(R.id.btn_mp3_to_pcm) }
    private val btnPCMToMp3 by lazy { findViewById<Button>(R.id.btn_pcm_to_mp3) }
    private val btnPlayMp3 by lazy { findViewById<Button>(R.id.btn_play_mp3) }
    private val btnPlayPCM by lazy { findViewById<Button>(R.id.btn_play_pcm) }
    private val progressBar by lazy { findViewById<ProgressBar>(R.id.progress_bar) }

    private val targetDir: File? by lazy { applicationContext.getExternalFilesDir("cache")?.absoluteFile }
    private val sourcePCMFile: File? by lazy { File(targetDir?.absolutePath + "/fhhkdlk_pcm.pcm") }
    private val sourceMP3File: File? by lazy { File(targetDir?.absolutePath + "/fhhkdlk_mp3.mp3") }
    private val targetMP3File: File? by lazy { File(targetDir?.absolutePath + "/target_mp3.mp3") }
    private val targetPCMFile: File? by lazy { File(targetDir?.absolutePath + "/target_pcm.pcm") }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_file_transform)
        updateFileStateDesc()


        btnCopy.setOnClickListener {
            copyResources()
            updateFileStateDesc()
        }

        btnPCMToMp3.setOnClickListener {
            transformPCMToMp3(targetMP3File, sourcePCMFile) {
                progressBar.post { progressBar.setProgress(it, true) }
            }
        }

        btnPlayPCM.setOnClickListener {
            PCMPlayer.asyncPlayPCMFile(targetPCMFile?.absolutePath)
        }

        btnMp3ToPCM.setOnClickListener {

        }

        btnPlayMp3.setOnClickListener {

        }
    }

    private fun updateFileStateDesc() {
        etSourcePCM.setText("exists=${sourcePCMFile?.exists()} path=${sourcePCMFile?.absolutePath}")
        etSourceMP3.setText("exists=${sourceMP3File?.exists()} path=${sourceMP3File?.absolutePath}")
        etTargetMP3.setText("exists=${targetMP3File?.exists()} path=${targetMP3File?.absolutePath}")
        etTargetPCM.setText("exists=${targetPCMFile?.exists()} path=${targetPCMFile?.absolutePath}")
    }

    private fun copyResources () {
        Log.d(TAG, "copy start")
        copyAssetToSDCard(this, R.raw.fhhkdlk_pcm, sourcePCMFile?.absolutePath)
        copyAssetToSDCard(this, R.raw.fhhkdlk_mp3, sourceMP3File?.absolutePath)
        Log.d(TAG, "copy end")
    }


    private fun transformPCMToMp3(mp3File: File?, pcmFile: File?, callback: (Int)-> Unit) {
        if (mp3File == null || pcmFile == null) {
            Log.e(TAG, "transformPCMToMp3: $pcmFile $mp3File")
            return
        }
        lifecycleScope.launch(Dispatchers.IO) {
            // 执行一些耗时操作...
            val ret = NativeUtils.transformPCMToMp3(mp3File.absolutePath, pcmFile.absolutePath, object:
                IProgress {
                override fun onProgress(value: Int) {
                    Log.d(TAG, "onProgress: $value")
                    callback(value)
                }
            })
        }
    }

    private fun transformMp3ToPCM(mp3File: File, pcmFile: File) {
        val ret = NativeUtils.transformMp3ToPCM(mp3File.absolutePath, pcmFile.absolutePath, object:
            IProgress {
            override fun onProgress(value: Int) {
                Toast.makeText(applicationContext, "hhh $value", Toast.LENGTH_SHORT).show()
            }
        })
        Toast.makeText(this, "hhh $ret", Toast.LENGTH_LONG).show()
    }
}
package com.wurengao.demos.feature

import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.wurengao.demos.R

class StoryCreateGuideActivity : AppCompatActivity() {


    private val coverList = listOf(
        R.mipmap.sj,
        R.mipmap.jh,
        R.mipmap.cj,
        R.mipmap.hx
    )

    private val coverContainerLayout : CoverContainerLayout by lazy { findViewById(R.id.cover_container) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_story_create_guide)
        val context = baseContext

        coverContainerLayout.post {
            coverList.forEach {
                coverContainerLayout.addView(StoryCardView(context).apply {
                    var widthRate = 0f
                    var whRate = 0f
                    if (coverList.size == 4) {
                        widthRate = 0.4f
                        whRate = 1f
                    } else {
                        widthRate = 0.5f
                        whRate = 0.75f
                    }
                    val width = coverContainerLayout.width * widthRate
                    val height = width / whRate
                    layoutParams = LinearLayout.LayoutParams(width.toInt(), height.toInt())
                    radius = width * 40f / 140
                    setImage(it)
                })
            }
        }
    }
}
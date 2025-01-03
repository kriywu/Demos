package com.wurengao.demos.core

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.dynamicanimation.animation.SpringAnimation
import com.wurengao.demos.R

class UIDesignActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_uidesign)

        val btn = findViewById<Button>(R.id.btn).apply {
        }


        btn.translationX = (-130) * 3f

        SpringAnimation(btn, SpringAnimation.TRANSLATION_X, 0f).apply {
            spring.setStiffness(100f)
            spring.setDampingRatio(0.1f)
            setStartVelocity(0f)
            start()
        }
    }

}
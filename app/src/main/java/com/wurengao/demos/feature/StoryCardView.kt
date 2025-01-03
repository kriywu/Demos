package com.wurengao.demos.feature

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.cardview.widget.CardView

/**
 * Created by wurengao on 2024/9/4
 * @author wurengao@bytedance.com
 */

class StoryCardView(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0):
    CardView(context, attrs, defStyleAttr) {

    private val imageView by lazy {
        val imageView = InnerImageView(this.context)
        imageView.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT )
        imageView.scaleType = ImageView.ScaleType.FIT_XY
        addView(imageView)
        imageView
    }

    public fun setImage(res: Int) {
        imageView.setImageResource(res)
    }
}

class InnerImageView(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0):
    androidx.appcompat.widget.AppCompatImageView(context, attrs, defStyleAttr) {

    private val cornerRadius by lazy { measuredWidth * 40f / 140 }
    private val borderWidth by lazy { measuredWidth * 5f / 140 }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawStoke(canvas)
    }

    private fun drawStoke(canvas: Canvas) {
        canvas.drawRoundRect(
            RectF(
                0f + borderWidth / 2 ,
                0f + borderWidth / 2 ,
                width.toFloat() - borderWidth/2,
                height.toFloat() - borderWidth/2
            ),
            cornerRadius - borderWidth / 2 ,
            cornerRadius - borderWidth / 2 ,
            Paint().apply {
                isAntiAlias = true
                style = Paint.Style.STROKE
                color = Color.WHITE
                strokeWidth = borderWidth
            })
    }
}
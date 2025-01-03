package com.wurengao.demos.feature

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet


/**
 * Created by wurengao on 2024/8/30
 * @author wurengao@bytedance.com
 */
class CardView(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0):
    androidx.appcompat.widget.AppCompatImageView(context, attrs, defStyleAttr) {

    private val cornerRadius by lazy { measuredWidth * 40f / 140 }
    private val borderWidth by lazy { measuredWidth * 5f / 140 }


    override fun onDraw(canvas: Canvas) {
        clipCanvas(canvas)

        super.onDraw(canvas)
        drawStoke(canvas)
    }



    private fun clipCanvas(canvas: Canvas) {
        val clipPath = Path()
        val rectF = RectF()

        rectF.set(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat())
        clipPath.addRoundRect(rectF, cornerRadius, cornerRadius, Path.Direction.CW);
        canvas.clipPath(clipPath);
        canvas.drawRect(rectF, Paint().apply {
            isAntiAlias = true
            isDither = true
        });
    }

    private fun drawStoke(canvas: Canvas) {
        canvas.drawRoundRect(
            getStoke(),
            cornerRadius - borderWidth / 2 ,
            cornerRadius - borderWidth / 2 ,
            Paint().apply {
                isAntiAlias = true
                style = Paint.Style.STROKE
                color = Color.WHITE
                strokeWidth = borderWidth
            })
    }

    private fun getStoke() = RectF(
        0f + borderWidth / 2 ,
        0f + borderWidth / 2 ,
        width.toFloat() - borderWidth/2,
        height.toFloat() - borderWidth/2
    )

    //    private fun getRoundedCornerBitmap(bitmap: Bitmap): Bitmap {
//        val output = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
//
//        val targetRect = RectF(0f, 0f, 450f, 600f)
//
//        val path = Path().apply {
//            addRoundRect(targetRect, cornerRadius, cornerRadius, Path.Direction.CW)
//            close()
//        }
//        val canvas = Canvas(output)
//        val paint = Paint().apply { isAntiAlias = true }
//        canvas.drawPath(path, paint)
//        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
//        canvas.drawARGB(0, 0, 0, 0)
//        canvas.drawBitmap(bitmap, getSourceRetF(bitmap.width, bitmap.height), targetRect, paint)
//        return output
//    }

//    private fun getSourceRetF(sourceWidth: Int, sourceHeight: Int): Rect {
//        val sourceRect = if (height > width) {
//            val delta = (1 - width.toFloat() / height) * sourceWidth / 2
//            Rect(delta.toInt(), 0, sourceWidth - delta.toInt(), sourceHeight)
//        } else {
//            val delta = (1 - height.toFloat() / width) * sourceHeight / 2
//            Rect(0 , delta.toInt(), sourceWidth, sourceHeight - delta.toInt())
//        }
//
//        return sourceRect
//    }

}
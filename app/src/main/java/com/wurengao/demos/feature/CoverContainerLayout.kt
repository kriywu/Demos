package com.wurengao.demos.feature


import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.core.view.children

class CoverContainerLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    init { orientation = VERTICAL }

    private val TAG = "CoverContainerLayout"
    data class PositionInfo(val rotation: Int, var x: Int, var y: Int)

    private val centerX by lazy { measuredWidth / 2 }
    private val centerY by lazy { measuredHeight / 2 }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val childLeftTop = getChildAt(0)  ?: return
        val childWidth = childLeftTop.measuredWidth
        val childHeight = childLeftTop.measuredHeight
        val rate = childWidth / 140

        when(childCount) {
            1 -> onLayout1(childWidth, childHeight, rate)
            2,3 -> onLayout2(childWidth, childHeight, rate)
            4 -> onLayout4(childWidth, childHeight, rate)
            else -> onLayout4(childWidth, childHeight, rate)
        }
    }


    private fun onLayout1(childWidth: Int, childHeight: Int, rate: Int) {

        val positions: List<PositionInfo> = listOf(
            PositionInfo(-8, centerX - childWidth / 2, centerY - childHeight / 2 - 8 * rate),
        )
        onLayoutX(positions, false)
    }

    private fun onLayout2(childWidth: Int, childHeight: Int, rate: Int) {
        val positions: List<PositionInfo> = listOf(
            PositionInfo(-8, centerX - childWidth, centerY - childHeight / 2),
            PositionInfo(8, centerX - 28 * rate, centerY - childHeight / 2 + 28 * rate),
        )

        onLayoutX(positions, true)
    }

    private fun onLayout4(childWidth: Int, childHeight: Int, rate: Int) {
        // default position info
        val positions: List<PositionInfo> = listOf(
            PositionInfo(-8, centerX - childWidth, centerY - childHeight -8 * rate),
            PositionInfo(8, centerX - 10 * rate, centerY - childHeight),
            PositionInfo(16, centerX - childWidth, centerY - 20 * rate),
            PositionInfo(-8, centerX - 20 * rate, centerY - 20 * rate)
        )

        onLayoutX(positions, false)
    }

    private fun onLayoutX(positions: List<PositionInfo>, needReversed: Boolean = false) {
        val childLeftTop = getChildAt(0)  ?: return
        val childWidth = childLeftTop.measuredWidth
        val childHeight = childLeftTop.measuredHeight

        val list = if (needReversed) children.toList().reversed() else children.toList()
        list.forEachIndexed { index, view ->
            if (index == positions.size) return@forEachIndexed
            val pos = positions[index]
            view.layout(pos.x, pos.y, pos.x + childWidth, pos.y + childHeight)
            view.rotation = pos.rotation.toFloat()
        }

    }
}
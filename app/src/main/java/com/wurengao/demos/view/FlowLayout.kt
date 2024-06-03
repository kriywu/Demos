package com.wurengao.demos.view

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import kotlin.math.max

/**
 * Created by wurengao on 2024/6/2
 * @author wurengao@bytedance.com
 */
class FlowLayout : ViewGroup {

    private val lineViews = mutableListOf<List<View>>() // 每一行的View集合
    private val lineViewsHeight = mutableListOf<Int>() // 每一行的高度


    // 代码创建View的构造方法
    constructor(context: Context?): super(context) {
    }

    // XML 反射
    constructor(context: Context?, attributeSet: AttributeSet): super(context, attributeSet) {}

    // 主题
    constructor(context: Context?, attributeSet: AttributeSet, defStyle: Int): super(context, attributeSet, defStyle)

    private fun clearChildrenViewsState() {
        lineViews.clear()
        lineViewsHeight.clear()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        clearChildrenViewsState()

        var currentLineViews = mutableListOf<View>()
        var currentLineHeight = 0
        var currentUsedWidth = 0
//        var currentUsedHeight = 0

        var childrenTotalWidth = 0
        var childrenTotalHeight = 0


        // 度量孩子
        val childCount = childCount
        for (i in 0 until childCount) {
            val childView = getChildAt(i)

            // 将 layout_width 和 layout_height 和 ParentSpec 转化
            val childLP = childView.layoutParams
            val childWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec, paddingLeft + paddingRight, childLP.width)
            val childHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec, paddingTop + paddingBottom, childLP.height)
            childView.measure(childWidthMeasureSpec, childHeightMeasureSpec) // 这里需要传入的是 child 的 Spec
            if (paddingLeft + currentUsedWidth + childView.measuredWidth + paddingRight > measuredWidth) {
                lineViews.add(currentLineViews)
                lineViewsHeight.add(currentLineHeight)
                childrenTotalWidth = max(childrenTotalWidth, currentUsedWidth)
                childrenTotalHeight += currentLineHeight

                currentLineViews = mutableListOf<View>()
                currentLineHeight = 0
                currentUsedWidth = 0
            }

            currentUsedWidth += childView.measuredWidth
            currentLineHeight = max(childView.measuredHeight, currentLineHeight)
            currentLineViews.add(childView)
        }

        // 最后一行
        lineViews.add(currentLineViews)
        lineViewsHeight.add(currentLineHeight)
        childrenTotalHeight += currentLineHeight

        // 度量自己，根据实现的功能定义
        var measureWidth = MeasureSpec.getSize(widthMeasureSpec)
        var measureHeight = MeasureSpec.getSize(heightMeasureSpec)
        if (MeasureSpec.getMode(widthMeasureSpec) != MeasureSpec.EXACTLY) { measureWidth = childrenTotalWidth + paddingLeft + paddingRight}
        if (MeasureSpec.getMode(heightMeasureSpec) != MeasureSpec.EXACTLY) { measureHeight = childrenTotalHeight + paddingTop + paddingBottom }
        setMeasuredDimension(measureWidth, measureHeight)
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
    }
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var childLeft = paddingLeft
        var childTop = paddingTop

        for (i in lineViews.indices) {
            val currentLineViews = lineViews[i]

            for (j in currentLineViews.indices) {
                val childView: View = currentLineViews[j]
                childView.layout(childLeft, childTop, childLeft + childView.measuredWidth, childTop + childView.measuredHeight)
                childLeft += childView.width
            }
            childTop += lineViewsHeight[i]
            childLeft = paddingLeft
        }

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
    }

}
package com.moonwinston.motivationaltodolist.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

class AchievementPieChartView@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {

    private var percentage: Float = 0.0F
    private var borderStrokeWidth: Float = 40F
    private var progressiveStrokeWidth: Float = 20F

    override fun onDraw(canvas: Canvas?) {
        val centerX = measuredWidth / 2
        val centerY = measuredHeight / 2
        val width = centerX / 1.5
        val height = centerY / 1.5
        val left = centerX - width / 1.5
        val top = centerY - height / 1.5
        val right = centerX + width / 1.5
        val bottom = centerY + height / 1.5
        val rectF = RectF(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat())
        val paint = Paint()
        paint.style = Paint.Style.STROKE;

        drawPieChart(canvas, borderStrokeWidth, rectF, paint)
        drawProgressivePieChart(canvas, progressiveStrokeWidth, rectF, paint)
    }

    private fun drawPieChart(canvas: Canvas?, borderStrokeWidth: Float, rectF: RectF, paint: Paint) {
        paint.apply {
            strokeWidth = borderStrokeWidth;
            color = Color.parseColor("#8E8C8C")
        }
        canvas?.drawArc(rectF, 270F, 360F, false, paint)
    }

    private fun drawProgressivePieChart(canvas: Canvas?, progressiveStrokeWidth: Float, rectF: RectF, paint: Paint) {
        paint.apply {
            strokeWidth = progressiveStrokeWidth;
            color = Color.parseColor("#2E2D2D")
        }
        canvas?.drawArc(rectF, 270F, percentage * 360F, false, paint)
    }

    fun setPercentAndBoardWidthAndProgressiveWidth(percent: Float, borderWidth: Float, progressiveWidth: Float) {
        percentage = percent
        borderStrokeWidth = borderWidth
        progressiveStrokeWidth = progressiveWidth
        invalidate()
        requestLayout()
    }
}

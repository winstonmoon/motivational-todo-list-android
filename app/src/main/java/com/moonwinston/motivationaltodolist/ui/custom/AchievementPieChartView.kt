package com.moonwinston.motivationaltodolist.ui.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.moonwinston.motivationaltodolist.R
import com.moonwinston.motivationaltodolist.utils.nonExistDate

class AchievementPieChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {
    var percentage = 0.0F
    private var borderStrokeWidth = 0.0F
    private var progressiveStrokeWidth = 0.0F
    private var pieChartColor = 0
    private var progressivePieChartColor = 0
    var pieChartViewDate = nonExistDate()

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.AchievementPieChart,
            0, 0).apply {

            try {
                borderStrokeWidth = getFloat(R.styleable.AchievementPieChart_borderStrokeWidth, 0.0F)
                progressiveStrokeWidth = getFloat(R.styleable.AchievementPieChart_progressiveStrokeWidth, 0.0F)
                pieChartColor = getColor(R.styleable.AchievementPieChart_pieChartColor, 0)
                progressivePieChartColor = getColor(R.styleable.AchievementPieChart_progressivePieChartColor, 0)
            } finally {
                recycle()
            }
        }
    }

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

        drawPieChart(canvas, rectF, paint)
        drawProgressivePieChart(canvas, rectF, paint)
    }

    private fun drawPieChart(
        canvas: Canvas?,
        rectF: RectF,
        paint: Paint
    ) {
        paint.apply {
            strokeWidth = borderStrokeWidth
            color = pieChartColor
        }
        canvas?.drawArc(rectF, 270F, 360F, false, paint)
    }

    private fun drawProgressivePieChart(
        canvas: Canvas?,
        rectF: RectF,
        paint: Paint
    ) {
        paint.apply {
            strokeWidth = progressiveStrokeWidth
            color = progressivePieChartColor
        }
        canvas?.drawArc(rectF, 270F, percentage * 360F, false, paint)
    }

    fun updatePercentage(percent: Float) {
        percentage = percent
        invalidate()
        requestLayout()
    }
}

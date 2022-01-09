package com.moonwinston.motivationaltodolist.extension

import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.moonwinston.motivationaltodolist.MonthEnum
import com.moonwinston.motivationaltodolist.R
import com.moonwinston.motivationaltodolist.ui.custom.AchievementPieChartView
import com.moonwinston.motivationaltodolist.utils.ContextUtil
import java.util.*
import kotlin.math.roundToInt

@BindingAdapter("bind:background")
fun setBackgroundColor(view: View, colorId: Int) {
    view.setBackgroundColor(view.context.resources.getColor(colorId))
}

@BindingAdapter("bind:adapter")
fun setAdapter(view: RecyclerView, baseAdapter: RecyclerView.Adapter<*>) {
    view.adapter = baseAdapter
}

@BindingAdapter("bind:alpha")
fun setAlphaByRate(view: AchievementPieChartView, rate: Float) {
    view.alpha = when (rate) {
        0.0F -> 0.2F
        else -> 1.0F
    }
}

@BindingAdapter("bind:achievementRate")
fun setAchievementRate(view: TextView, rate: Float) {
    val achievementRate = (rate * 100).roundToInt().toString()
    view.text = "$achievementRate%"
}

@BindingAdapter("bind:setPercentage")
fun setPercentage(view: AchievementPieChartView, rate: Float) {
    view.percentage = rate
    view.invalidate()
    view.requestLayout()
}


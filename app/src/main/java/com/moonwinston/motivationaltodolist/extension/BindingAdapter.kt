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

@BindingAdapter("bind:dailyTitleTextByLanguage")
fun setDailyTitleTextByLanguage(view: TextView, language: Int) {
    val resources = view.context.resources
    val cal = Calendar.getInstance()
    val date = cal.get(Calendar.DATE)
    val month = cal.get(Calendar.MONTH)
    val parsedMonth = resources.getString(MonthEnum.values()[month].monthAbbreviation)
    val year = cal.get(Calendar.YEAR)
    val today = resources.getString(R.string.text_today)
    val wordYear = resources.getString(R.string.label_year)
    val wordDay = resources.getString(R.string.label_day)
    view.text =
        when (language) {
            ContextUtil.ENGLISH -> "$today, $parsedMonth $date, $year"
            else -> "$year$wordYear $parsedMonth $date$wordDay $today"
        }
}


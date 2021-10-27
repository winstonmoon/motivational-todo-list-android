package com.moonwinston.motivationaltodolist.data

import android.content.Context
import androidx.preference.PreferenceManager

const val THEME = "theme"
const val LANGUAGE = "language"
const val NOTIFY = "notify"
const val COACH_DAILY = "coachDaily"
const val COACH_WEEKLY = "coachWeekly"
const val COACH_MONTHLY = "coachMonthly"

class SharedPref(context: Context) {

    private val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)

    fun setCoachDailyAsDismissed(isDismissed: Boolean) {
        sharedPref.edit().putBoolean(COACH_DAILY, isDismissed).apply()
    }

    fun isCoachDailyDismissed(): Boolean = sharedPref.getBoolean(COACH_DAILY, false) ?: false

    fun setCoachWeeklyAsDismissed(isDismissed: Boolean) {
        sharedPref.edit().putBoolean(COACH_WEEKLY, isDismissed).apply()
    }

    fun isCoachWeeklyDismissed(): Boolean = sharedPref.getBoolean(COACH_WEEKLY, false) ?: false

    fun setCoachMonthlyAsDismissed(isDismissed: Boolean) {
        sharedPref.edit().putBoolean(COACH_MONTHLY, isDismissed).apply()
    }

    fun isCoachMonthlyDismissed(): Boolean = sharedPref.getBoolean(COACH_MONTHLY, false) ?: false

    fun saveTheme(theme: Int) {
        sharedPref.edit().putInt(THEME, theme).apply()
    }

    fun getTheme(): Int = sharedPref.getInt(THEME, 0) ?: 0

    fun saveLanguage(language: Int) {
        sharedPref.edit().putInt(LANGUAGE, language).apply()
    }

    fun getLanguage(): Int = sharedPref.getInt(LANGUAGE, 0) ?: 0

    fun saveNotify(notify: Int) {
        sharedPref.edit().putInt(NOTIFY, notify).apply()
    }

    fun getNotify(): Int = sharedPref.getInt(NOTIFY, 0) ?: 0
}
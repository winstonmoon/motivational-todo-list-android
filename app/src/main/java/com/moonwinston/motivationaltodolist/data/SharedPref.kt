package com.moonwinston.motivationaltodolist.data

import android.content.Context
import androidx.preference.PreferenceManager

const val THEME = "theme"
const val LANGUAGE = "language"
const val NOTIFY = "notify"

class SharedPref(context : Context) {

    private val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)

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
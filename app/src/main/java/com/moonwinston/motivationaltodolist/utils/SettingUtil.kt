package com.moonwinston.motivationaltodolist.utils

import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat

fun setNightMode(theme: Int) {
    when (theme) {
        1 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        2 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        else ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY)
            }
    }
}

fun setLanguage(language: Int) {
    val language =
        when (language) {
            1 -> "en"
            2 -> "ko"
            3 -> "ja"
            4 -> "zh-Hans"
            else -> ""
        }
    val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(language)
    AppCompatDelegate.setApplicationLocales(appLocale)
}
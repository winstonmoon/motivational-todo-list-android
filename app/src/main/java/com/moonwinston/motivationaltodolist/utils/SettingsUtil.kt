package com.moonwinston.motivationaltodolist.utils

import android.content.res.Resources
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat

fun setNightMode(theme: Int) {
    when (Theme.values()[theme]) {
        Theme.FOLLOW_SYSTEM -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY)
            }
        }
        Theme.DARK -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        Theme.LIGHT -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}

enum class Theme(val index: Int) {
    FOLLOW_SYSTEM(0),
    DARK(1),
    LIGHT(2)
}

fun setLanguage(language: Int) {
    val list =
        when (Language.values()[language]) {
            Language.FOLLOW_SYSTEM -> {
                val deviceLocale = Resources.getSystem().configuration.locales[0]
                val deviceLanguage = deviceLocale.language
                val availableLanguageList = listOf("en", "ko", "ja", "zh-Hans")
                if (availableLanguageList.contains(deviceLanguage)) deviceLanguage else "en"
            }
            Language.ENGLISH -> "en"
            Language.KOREAN -> "ko"
            Language.JAPANESE -> "ja"
            Language.SIMPLIFIED_CHINESE -> "zh-Hans"
        }
    val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(list)
    AppCompatDelegate.setApplicationLocales(appLocale)
}

enum class Language(val index: Int) {
    FOLLOW_SYSTEM(0),
    ENGLISH(1),
    KOREAN(2),
    JAPANESE(3),
    SIMPLIFIED_CHINESE(4)
}

enum class Notification(val index: Int, val time: Long) {
    OFF(0, 0L),
    FIVE_MIN(1, 5L),
    TEN_MIN(2, 10L),
    FIFTEEN_MIN(3, 15L),
    THIRTY_MIN(4, 30L),
    ONE_HOUR(5, 60L)
}
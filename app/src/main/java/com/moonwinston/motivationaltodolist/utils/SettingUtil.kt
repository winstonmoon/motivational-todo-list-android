package com.moonwinston.motivationaltodolist.utils

import android.content.res.Resources
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import java.util.*

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
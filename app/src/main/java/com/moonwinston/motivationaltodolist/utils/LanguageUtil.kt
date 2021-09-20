package com.moonwinston.motivationaltodolist.utils

import android.content.res.Configuration
import java.util.*

class LanguageUtil {
    fun setLanguague(language: String) {
        val locale = Locale(language)
        val config = Configuration()
        config.setLocale(locale)
    }
}
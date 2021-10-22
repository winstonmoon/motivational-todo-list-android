package com.moonwinston.motivationaltodolist.utils

import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.LocaleList
import java.util.*

class ContextUtil(base: Context) : ContextWrapper(base) {

    companion object {

        fun updateLocale(c: Context, language: Int): ContextWrapper {
            var context = c
            val resources: Resources = context.resources
            val configuration: Configuration = resources.configuration

            val localeToSwitchTo = when (language) {
                1 -> Locale.US
                2 -> Locale.KOREA
                3 -> Locale.JAPAN
                4 -> Locale.SIMPLIFIED_CHINESE
                else -> {
                    var deviceLanguage =
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) Resources.getSystem().configuration.locales[0]
                        else Resources.getSystem().configuration.locale
                    val availableLanguageList =
                        listOf(Locale.US, Locale.KOREA, Locale.JAPAN, Locale.SIMPLIFIED_CHINESE)
                    if (availableLanguageList.contains(deviceLanguage)) deviceLanguage else Locale.ENGLISH
                }
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                val localeList = LocaleList(localeToSwitchTo)
                LocaleList.setDefault(localeList)
                configuration.setLocales(localeList)
            } else {
                configuration.locale = localeToSwitchTo
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
                context = context.createConfigurationContext(configuration)
            } else {
                resources.updateConfiguration(configuration, resources.displayMetrics)
            }
            return ContextUtil(context)
        }
    }
}
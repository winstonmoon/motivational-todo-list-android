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
        const val ENGLISH = 1

        fun updateLocale(c: Context, language: Int): ContextWrapper {
            var context = c
            val resources: Resources = context.resources
            val configuration: Configuration = resources.configuration

            val localeToSwitchTo = when (language) {
                1 -> Locale.ENGLISH
                2 -> Locale.KOREAN
                3 -> Locale.JAPANESE
                4 -> Locale.SIMPLIFIED_CHINESE
                else -> {
                    val deviceLocale = Resources.getSystem().configuration.locales[0]
                    var deviceLanguage = deviceLocale.language
                    val deviceScript = deviceLocale.script
                    deviceLanguage += if (deviceScript.isNotEmpty()) "-$deviceScript" else ""
                    val languageTag = Locale.forLanguageTag(deviceLanguage)
                    val availableLanguageList = listOf("en", "ko", "ja", "zh-Hans")
                    if (availableLanguageList.contains(deviceLanguage)) languageTag else Locale.ENGLISH
                }
            }

            val localeList = LocaleList(localeToSwitchTo)
            LocaleList.setDefault(localeList)
            configuration.setLocales(localeList)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
                context = context.createConfigurationContext(configuration)
            } else {
                resources.updateConfiguration(configuration, resources.displayMetrics)
            }
            return ContextUtil(context)
        }
    }
}
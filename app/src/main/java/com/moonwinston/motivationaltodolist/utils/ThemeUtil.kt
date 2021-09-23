package com.moonwinston.motivationaltodolist.utils

import androidx.appcompat.app.AppCompatDelegate

class ThemeUtil {

    fun setTheme(theme: String) {
        when (theme) {
//            "DARK" -> {
            "dark" -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
//            "LIGHT" -> {
            "light" -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }
}
package com.moonwinston.motivationaltodolist.data

import android.content.Context
import android.content.SharedPreferences
import com.moonwinston.motivationaltodolist.data.PreferenceHelper
import com.moonwinston.motivationaltodolist.data.PreferenceHelper.set
import com.moonwinston.motivationaltodolist.data.PreferenceHelper.get

class SharedManager(context: Context) {
    private val prefs: SharedPreferences = PreferenceHelper.defaultPrefs(context)

    fun saveLanguage(language: String) {
        prefs["language"] = language
    }

    //TODO get locale
    fun getLanguage(): String = prefs["language"]

    fun saveTheme(theme: String) {
        prefs["theme"] = theme
    }

    fun getTheme(): String = prefs["theme"]

    fun saveNotify(time: Int) {
        prefs["notify"] = time
    }

    fun getNotify(): Int = prefs["notify"]

//    fun saveCurrentUser(user: User) {
//        prefs["name"] = user.name
//        prefs["age"] = user.age
//        prefs["email"] = user.email
//        prefs["password"] = user.password
//        prefs["isMarried"] = user.isMarried
//    }
//
//    fun getCurrentUser(): User {
//        return User().apply {
//            name = prefs["name", ""]
//            age = prefs["age", 0]
//            email = prefs["email", ""]
//            password = prefs["password", ""]
//            isMarried = prefs["isMarried", false]
//        }
//    }
}
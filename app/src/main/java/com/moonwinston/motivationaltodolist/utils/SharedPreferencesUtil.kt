package com.moonwinston.motivationaltodolist.utils

import android.content.Context
import android.content.SharedPreferences
import com.moonwinston.motivationaltodolist.utils.PreferenceHelper.set
import com.moonwinston.motivationaltodolist.utils.PreferenceHelper.get

class SharedPreferencesUtil(context: Context) {
    private val prefs: SharedPreferences = PreferenceHelper.defaultPrefs(context)

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
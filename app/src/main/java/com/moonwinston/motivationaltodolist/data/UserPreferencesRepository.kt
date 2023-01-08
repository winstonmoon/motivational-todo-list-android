package com.moonwinston.motivationaltodolist.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserPreferencesRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    private object PreferencesKeys {
        val COACH_DAILY = booleanPreferencesKey("coach_daily")
        val COACH_WEEKLY = booleanPreferencesKey("coach_weekly")
        val COACH_MONTHLY = booleanPreferencesKey("coach_monthly")

        val NOTIFY = intPreferencesKey("notify")
        val THEME = intPreferencesKey("theme")
        val LANGUAGE = intPreferencesKey("language")
    }

    val fetchDailyCoachMarkDismissedStatusFlow: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.COACH_DAILY] ?: false
        }

    suspend fun updateDailyCoachMarkDismissedStatusFlow(dismissDailyCoachMark: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.COACH_DAILY] = dismissDailyCoachMark
        }
    }

    val fetchWeeklyCoachMarkDismissedStatusFlow: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.COACH_WEEKLY] ?: false
        }

    suspend fun updateWeeklyCoachMarkDismissedStatusFlow(dismissWeeklyCoachMark: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.COACH_WEEKLY] = dismissWeeklyCoachMark
        }
    }

    val fetchMonthlyCoachMarkDismissedStatusFlow: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.COACH_MONTHLY] ?: false
        }

    suspend fun updateMonthlyCoachMarkDismissedStatusFlow(dismissMonthlyCoachMark: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.COACH_MONTHLY] = dismissMonthlyCoachMark
        }
    }

    val fetchNotifySettingFlow: Flow<Int> = dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.NOTIFY] ?: 0
        }

    suspend fun updateNotifySettingFlow(notify: Int) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.NOTIFY] = notify
        }
    }

    val fetchThemeSettingFlow: Flow<Int> = dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.THEME] ?: 0
        }

    suspend fun updateThemeSettingFlow(theme: Int) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.THEME] = theme
        }
    }

    val fetchLanguageSettingFlow: Flow<Int> = dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.LANGUAGE] ?: 0
        }

    suspend fun updateLanguageSettingFlow(language: Int) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.LANGUAGE] = language
        }
    }
}
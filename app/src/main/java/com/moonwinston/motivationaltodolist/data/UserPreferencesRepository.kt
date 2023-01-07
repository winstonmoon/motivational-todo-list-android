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
    private val COACH_DAILY = booleanPreferencesKey("example_counter")
    private val COACH_WEEKLY = booleanPreferencesKey("example_counter")
    private val COACH_MONTHLY = booleanPreferencesKey("example_counter")

    private val NOTIFY = intPreferencesKey("notify")
    private val THEME = intPreferencesKey("example_counter")
    private val LANGUAGE = intPreferencesKey("example_counter")

    val getNotifyFlow: Flow<Int> = dataStore.data
        .map { preferences ->
            preferences[NOTIFY] ?: 0
        }

    suspend fun setNotifyFlow() {
        dataStore.edit { settings ->
            val notifyValue = settings[NOTIFY] ?: 0
            settings[NOTIFY] = notifyValue
        }
    }
}
package com.nitinnegi.weatherapp.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

suspend fun DataStore<Preferences>.write(weather: String) {
    edit { preferences ->
        preferences[stringPreferencesKey(WEATHER_DETAILS)] = weather
    }
}

fun DataStore<Preferences>.read(): Flow<String> {
    return data.map { preferences ->
        preferences[stringPreferencesKey(WEATHER_DETAILS)] ?: "No Value"
    }
}

private const val WEATHER_DETAILS = "weather_details"

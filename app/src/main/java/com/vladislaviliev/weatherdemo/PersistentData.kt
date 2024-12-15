package com.vladislaviliev.weatherdemo

import android.content.SharedPreferences
import com.vladislaviliev.weatherdemo.weather.Location

class PersistentData {
    private val locationKey = "location"

    fun save(location: Location, storage: SharedPreferences) {
        storage.edit().putString(locationKey, location.name).apply()
    }

    fun load(storage: SharedPreferences) = Location.valueOf(storage.getString(locationKey, null) ?: Location.SOFIA.name)
}
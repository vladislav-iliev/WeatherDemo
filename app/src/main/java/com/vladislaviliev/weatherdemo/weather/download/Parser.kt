package com.vladislaviliev.weatherdemo.weather.download

import com.vladislaviliev.weatherdemo.weather.Forecast
import com.vladislaviliev.weatherdemo.weather.Location
import com.vladislaviliev.weatherdemo.weather.Sensor
import com.vladislaviliev.weatherdemo.weather.UnitTemp
import com.vladislaviliev.weatherdemo.weather.UnitWind
import org.json.JSONObject
import java.io.IOException
import java.time.LocalDateTime
import java.util.function.Function

class Parser {
    private val keyError = "error"
    private val keyReason = "reason"
    private val keyUnits = "hourly_units"
    private val keyHourly = "hourly"
    private val keyTime = "time"
    private val keyTemp = "temperature_2m"
    private val keyWeather = "weathercode"
    private val keyWind = "windspeed_10m"

    private fun isErroneous(data: JSONObject) = data.optBoolean(keyError)

    private fun getErrorMessage(data: JSONObject): String = data.getString(keyReason)

    private fun getUnitTemp(data: JSONObject) = UnitTemp.values().first { it.symbol == data.getJSONObject(keyUnits).getString(keyTemp) }

    private fun getUnitWind(data: JSONObject) = UnitWind.values().first { it.symbol == data.getJSONObject(keyUnits).getString(keyWind) }

    private fun <T> getMeasurements(data: JSONObject, typeKey: String, transformer: Function<String, T>): List<T> {
        val array = data.getJSONObject(keyHourly).getJSONArray(typeKey)
        val length = array.length()
        return (0 until length).map { transformer.apply(array.getString(it)) }
    }

    private fun getSensors(data: JSONObject): List<Sensor> {
        val times = getMeasurements(data, keyTime) { LocalDateTime.parse(it) }
        val temps = getMeasurements(data, keyTemp) { it.toDouble() }
        val weathers = getMeasurements(data, keyWeather) { it.toInt() }
        val winds = getMeasurements(data, keyWind) { it.toDouble() }
        val unitTemp = getUnitTemp(data)
        val unitWind = getUnitWind(data)
        return (times.indices).map { Sensor(times[it], temps[it], weathers[it], winds[it], unitTemp, unitWind) }
    }

    fun getForecast(location: Location, data: String): Forecast {
        val json = JSONObject(data)
        if (isErroneous(json)) throw IOException(getErrorMessage(json))
        return Forecast(location, getSensors(json))
    }
}
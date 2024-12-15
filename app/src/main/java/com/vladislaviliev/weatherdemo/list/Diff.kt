package com.vladislaviliev.weatherdemo.list

import androidx.recyclerview.widget.DiffUtil
import com.vladislaviliev.weatherdemo.weather.Sensor
import kotlin.math.abs

class Diff : DiffUtil.ItemCallback<Sensor>() {
    override fun areItemsTheSame(oldItem: Sensor, newItem: Sensor) = oldItem.time == newItem.time

    override fun areContentsTheSame(oldItem: Sensor, newItem: Sensor) =
        oldItem.temp.equalsDelta(newItem.temp)
                && oldItem.weather == newItem.weather
                && oldItem.wind.equalsDelta(newItem.wind)
                && oldItem.unitTemp == newItem.unitTemp
                && oldItem.unitWind == newItem.unitWind

    private fun Double.equalsDelta(other: Double) = abs(this / other - 1) < 0.000001
}
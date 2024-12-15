package com.vladislaviliev.weatherdemo.list

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vladislaviliev.weatherdemo.R
import com.vladislaviliev.weatherdemo.weather.Sensor

class Row(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val time = itemView.findViewById<TextView>(R.id.time)
    private val temperature = itemView.findViewById<TextView>(R.id.temperature)
    private val weather = itemView.findViewById<TextView>(R.id.weather)
    private val wind = itemView.findViewById<TextView>(R.id.wind)

    fun bind(sensor: Sensor) {
        val context = itemView.context
        time.text = context.getString(R.string.list_sensor_time, sensor.time.toString())
        temperature.text = context.getString(R.string.list_sensor_temperature, sensor.temp.toString(), sensor.unitTemp.symbol)
        weather.text = context.getString(R.string.list_sensor_weather, sensor.weather.toString())
        wind.text = context.getString(R.string.list_sensor_windspeed, sensor.wind.toString(), sensor.unitWind.symbol)
    }
}
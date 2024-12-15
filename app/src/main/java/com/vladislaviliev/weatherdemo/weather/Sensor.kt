package com.vladislaviliev.weatherdemo.weather

import java.time.LocalDateTime

data class Sensor(
    val time: LocalDateTime,
    val temp: Double,
    val weather: Int,
    val wind: Double,
    val unitTemp: UnitTemp,
    val unitWind: UnitWind
)
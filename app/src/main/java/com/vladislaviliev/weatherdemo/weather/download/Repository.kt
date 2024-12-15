package com.vladislaviliev.weatherdemo.weather.download

import retrofit2.http.GET
import retrofit2.http.Query

fun interface Repository {
    @GET("v1/forecast?hourly=temperature_2m,weathercode,windspeed_10m")
    suspend fun getRaw(@Query("latitude") lat: Double, @Query("longitude") lng: Double): String
}
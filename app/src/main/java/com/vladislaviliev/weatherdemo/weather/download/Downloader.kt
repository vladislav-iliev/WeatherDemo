package com.vladislaviliev.weatherdemo.weather.download

import com.vladislaviliev.weatherdemo.weather.Location
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.create

object Downloader {
    private val api = Retrofit.Builder()
        .baseUrl("https://api.open-meteo.com/")
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()
        .create<Repository>()

    private suspend fun downloadRaw(location: Location) = withContext(Dispatchers.IO) { api.getRaw(location.lat, location.lng) }

    suspend fun download(location: Location) = Parser().getForecast(location, downloadRaw(location))
}
package com.nitinnegi.weatherapp.data.repository

import com.nitinnegi.weatherapp.domain.model.WeatherModel

interface IWeatherRepo {
    suspend fun weather(
        url: String,
        latitude: String,
        longitude: String,
        hourly: String
    ): WeatherModel
}

package com.nitinnegi.weatherapp.data.repository

import com.nitinnegi.weatherapp.data.remote.WeatherApi
import com.nitinnegi.weatherapp.domain.model.WeatherModel

class WeatherRepo(
    private val api: WeatherApi
) : IWeatherRepo {

    override suspend fun weather(
        url: String,
        latitude: String,
        longitude: String,
        hourly: String
    ): WeatherModel {
        return api.getWeatherDetail(url, latitude, longitude, hourly)
    }
}

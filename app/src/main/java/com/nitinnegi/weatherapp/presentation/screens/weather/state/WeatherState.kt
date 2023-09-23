package com.nitinnegi.weatherapp.presentation.screens.weather.state

import com.nitinnegi.weatherapp.domain.model.WeatherModel

data class WeatherState(
    var isLoading : Boolean = false,
    var success : Int = 0,
    var data : WeatherModel.Hourly = WeatherModel.Hourly(),
    var error : String = "",
    var internet : Boolean = false
)

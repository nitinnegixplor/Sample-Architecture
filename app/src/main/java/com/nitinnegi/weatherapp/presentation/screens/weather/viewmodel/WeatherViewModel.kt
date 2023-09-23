package com.nitinnegi.weatherapp.presentation.screens.weather.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.nitinnegi.weatherapp.data.local.read
import com.nitinnegi.weatherapp.data.local.write
import com.nitinnegi.weatherapp.domain.model.WeatherModel
import com.nitinnegi.weatherapp.domain.usecase.weather.WeatherUseCase
import com.nitinnegi.weatherapp.presentation.screens.weather.state.WeatherState
import com.nitinnegi.weatherapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherUseCase: WeatherUseCase,
    private val dataStore: DataStore<Preferences>
) : ViewModel() {

    private val _state = mutableStateOf(WeatherState())
    val state: State<WeatherState> = _state
    private var job: Job? = null

    fun getWeatherDetails(
        url: String,
        latitude: String,
        longitude: String,
        hourly: String
    ) {
        job?.cancel()
        job = viewModelScope.launch(Dispatchers.IO) {
            weatherUseCase.invoke(
                url = url,
                latitude = latitude,
                longitude = longitude,
                hourly = hourly
            ).onEach { result ->

                when (result) {

                    is Resource.Loading -> {
                        _state.value = WeatherState(
                            isLoading = true,
                            internet = false
                        )
                    }

                    is Resource.Error -> {
                        _state.value = WeatherState(
                            isLoading = false,
                            internet = false,
                            success = ERROR_HTTP,
                        )
                    }

                    is Resource.Internet -> {
                        _state.value = WeatherState(
                            internet = true,
                            isLoading = false,
                            success = ERROR_INTERNET,
                            data = readWeatherFromDb().hourly
                        )
                    }

                    is Resource.Success -> {
                        writeWeatherToDb(result)

                        _state.value = WeatherState(
                            isLoading = false,
                            internet = false,
                            success = SUCCESS,
                            data = result.data?.hourly ?: WeatherModel.Hourly()
                        )
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    private suspend fun writeWeatherToDb(result: Resource.Success<WeatherModel>) {
        val weather = Gson().toJson(result.data)
        dataStore.write(weather)
    }

    private suspend fun readWeatherFromDb(): WeatherModel {
        val weather = dataStore.read()
        return Gson().fromJson(weather.first(), WeatherModel::class.java)
    }

    fun clearViewModel() {
        state.value.internet = false
        state.value.isLoading = false
        state.value.success = INITIAL
        state.value.data = WeatherModel.Hourly()
        state.value.error = ""
    }
}

const val INITIAL = 0
const val SUCCESS = 1
const val ERROR_HTTP = -1
const val ERROR_INTERNET = -2

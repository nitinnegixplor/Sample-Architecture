package com.nitinnegi.weatherapp.domain.usecase.weather

import android.util.Log
import com.nitinnegi.weatherapp.data.repository.IWeatherRepo
import com.nitinnegi.weatherapp.domain.model.WeatherModel
import com.nitinnegi.weatherapp.util.Resource
import com.nitinnegi.weatherapp.util.internetCheck
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class WeatherUseCase @Inject constructor(
    private val repository: IWeatherRepo,
) {
    operator fun invoke(
        url: String,
        latitude: String,
        longitude: String,
        hourly: String
    ): Flow<Resource<WeatherModel>> = flow {

        try {
            emit(Resource.Loading())
            val process = repository.weather(url, latitude, longitude, hourly)
            coroutineScope {
                emit(Resource.Success(process))
            }
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "Error"))
            e.localizedMessage?.let { Log.e("LOG :::", it) }
        } catch (e: IOException) {
            if (!internetCheck()) {
                emit(Resource.Internet("No Internet Connection!"))
                e.localizedMessage?.let { Log.e("LOG :::", it) }
            }
        }
    }
}

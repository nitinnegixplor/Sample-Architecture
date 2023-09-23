package com.nitinnegi.weatherapp.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

suspend fun internetCheck(): Boolean = withContext(Dispatchers.IO) {

    val runTime = Runtime.getRuntime()

    try {
        val ipProcess = runTime.exec("/system/bin/ping -c 1 www.google.com")
        val exitValue: Int = ipProcess.waitFor()
        return@withContext (exitValue == 0)

    } catch (e: IOException) {
        e.printStackTrace()
    } catch (e: InterruptedException) {
        e.printStackTrace()
    }

    return@withContext false
}

fun validation(latitude: String, longitude: String): Int {
    return if (latitude.trim().isNotEmpty() and longitude.trim().isNotEmpty()) {
        VALIDATION_SUCCESS
    } else {
        VALIDATION_FAILURE
    }
}

const val VALIDATION_SUCCESS = 1
const val VALIDATION_FAILURE = 2

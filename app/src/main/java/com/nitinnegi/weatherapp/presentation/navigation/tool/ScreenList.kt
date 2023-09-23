package com.nitinnegi.weatherapp.presentation.navigation.tool

const val MAIN_GRAPH_ROUTE = "main"

sealed class ScreenList(
    val route: String,
    val title: String? = null,
    val icon: Int? = null
) {
    object WeatherScreen : ScreenList("Weather_Screen")

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}
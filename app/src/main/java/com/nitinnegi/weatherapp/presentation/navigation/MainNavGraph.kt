package com.nitinnegi.weatherapp.presentation.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.nitinnegi.weatherapp.presentation.navigation.tool.MAIN_GRAPH_ROUTE
import com.nitinnegi.weatherapp.presentation.navigation.tool.ScreenList
import com.nitinnegi.weatherapp.presentation.screens.weather.screen.MainPage

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@ExperimentalAnimationApi
fun NavGraphBuilder.mainNavGraph() {
    navigation(startDestination = ScreenList.WeatherScreen.route, route = MAIN_GRAPH_ROUTE) {
        composable(
            route = ScreenList.WeatherScreen.route) {
            MainPage()
        }
    }
}

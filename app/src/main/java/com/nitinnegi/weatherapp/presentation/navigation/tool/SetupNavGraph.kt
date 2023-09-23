package com.nitinnegi.weatherapp.presentation.navigation.tool

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.nitinnegi.weatherapp.presentation.navigation.mainNavGraph

@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun SetupNavGraph(navHostController: NavHostController) {

    NavHost(
        navController = navHostController,
        startDestination = MAIN_GRAPH_ROUTE,
    ) {
        mainNavGraph()
    }
}

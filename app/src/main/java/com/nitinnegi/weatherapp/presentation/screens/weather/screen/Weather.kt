package com.nitinnegi.weatherapp.presentation.screens.weather.screen

import android.widget.Toast
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nitinnegi.weatherapp.R
import com.nitinnegi.weatherapp.presentation.screens.weather.state.WeatherState
import com.nitinnegi.weatherapp.presentation.screens.weather.viewmodel.ERROR_HTTP
import com.nitinnegi.weatherapp.presentation.screens.weather.viewmodel.ERROR_INTERNET
import com.nitinnegi.weatherapp.presentation.screens.weather.viewmodel.SUCCESS
import com.nitinnegi.weatherapp.presentation.screens.weather.viewmodel.WeatherViewModel
import com.nitinnegi.weatherapp.presentation.ui.theme.CardGrayColor
import com.nitinnegi.weatherapp.util.Constants
import com.nitinnegi.weatherapp.util.VALIDATION_FAILURE
import com.nitinnegi.weatherapp.util.VALIDATION_SUCCESS
import com.nitinnegi.weatherapp.util.validation

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun MainPage(viewModel: WeatherViewModel = hiltViewModel()) {

    val state = viewModel.state.value
    val scaffoldState = rememberScaffoldState()

    val infoDialog = remember {
        mutableStateOf(false)
    }

    if (infoDialog.value) {
        AlertDialog(
            onDismissRequest = {
                infoDialog.value = false
            },
            title = {
                Text(text = "Error")
            },
            text = {
                Text(text = "Latitude must be in range of -90 to 90°.")
            },
            buttons = {
                Row(
                    modifier = Modifier.padding(all = 8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            infoDialog.value = false
                            state.success = 0
                            viewModel.clearViewModel()
                        }
                    ) {
                        Text("Dismiss")
                    }
                }
            }
        )
    }

    Scaffold(scaffoldState = scaffoldState, snackbarHost = {
        SnackbarHost(it) {
            Snackbar(
                backgroundColor = Color.Red,
                contentColor = Color.White,
                actionColor = Color.White,
                snackbarData = it
            )
        }
    }, content = { padding ->

        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .background(
                    color = Color.White,
                )
                .padding(padding)
        ) {
            SearchBox(viewModel)

            when (state.success) {

                SUCCESS, ERROR_INTERNET -> {
                    ShowResult(state)
                }

                ERROR_HTTP -> {
                    infoDialog.value = true
                }
            }
        }
    })
}

@Composable
fun ShowResult(state: WeatherState) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color(0xFFEDEADE))
            .padding(start = 10.dp, top = 10.dp, end = 10.dp, bottom = 10.dp)
    ) {

        items(state.data.time.size) {

            Card(
                elevation = 2.dp,
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, CardGrayColor),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        bottom = 10.dp
                    )
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(
                        start = 20.dp, top = 20.dp, end = 20.dp, bottom = 20.dp
                    )
                ) {

                    Text(
                        text = "Time : " + state.data.time[it],
                        color = Color.Black,
                        fontSize = 18.sp,
                        modifier = Modifier.weight(2f)
                    )

                    Text(
                        text = "Temp : " + state.data.temperature2m[it],
                        color = Color.Black,
                        fontSize = 18.sp,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
fun SearchBox(viewModel: WeatherViewModel) {

    val context = LocalContext.current

    val latitude = remember {
        mutableStateOf("")
    }

    val longitude = remember {
        mutableStateOf("")
    }


    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .background(
                color = Color.White,
            )
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
        ) {
            Text(
                text = "Weather App",
                color = Color.Red,
                fontSize = 29.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 10.dp,
                        end = 10.dp,
                        top = 10.dp
                    ),
                value = latitude.value,
                onValueChange = { latitude.value = it },
                label = {
                    Text(
                        text = stringResource(R.string.latitude), color = Color.Black
                    )
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    backgroundColor = Color.White,
                    textColor = Color.Black,
                    leadingIconColor = Color.Black,
                    cursorColor = Color.Black,
                    focusedBorderColor = Color.Black,
                    unfocusedBorderColor = Color.Gray
                ),

                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number, imeAction = ImeAction.Next
                ),
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 10.dp,
                        end = 10.dp,
                        top = 10.dp
                    ),
                value = longitude.value,
                onValueChange = { longitude.value = it },

                label = {
                    Text(
                        text = stringResource(R.string.longitude), color = Color.Black
                    )
                },

                colors = TextFieldDefaults.outlinedTextFieldColors(
                    backgroundColor = Color.White,
                    textColor = Color.Black,
                    leadingIconColor = Color.Black,
                    cursorColor = Color.Black,
                    focusedBorderColor = Color.Black,
                    unfocusedBorderColor = Color.Gray
                ),

                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number, imeAction = ImeAction.Next
                ),
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 10.dp,
                        end = 10.dp,
                        top = 10.dp,
                        bottom = 20.dp
                    ), horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {
                        when (validation(
                            latitude.value, longitude.value
                        )) {
                            VALIDATION_SUCCESS -> {
                                viewModel.getWeatherDetails(
                                    Constants.FORECAST,
                                    latitude.value,
                                    longitude.value,
                                    Constants.HOURLY
                                )
                            }

                            VALIDATION_FAILURE -> {
                                Toast.makeText(
                                    context,
                                    "Latitude/Longitude can't be empty",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),

                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Red, contentColor = Color.White
                    )

                ) {
                    Text(
                        text = "Show Details",
                        fontSize = 18.sp,
                        modifier = Modifier.padding(
                            top = 10.dp, bottom = 10.dp
                        )
                    )
                }
            }
        }
    }
}

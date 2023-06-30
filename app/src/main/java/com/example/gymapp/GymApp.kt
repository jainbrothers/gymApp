package com.example.gymapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.flightsearchapp.ui.navigation.GymNavHost

@Composable
fun GymApp(
    modifier: Modifier = Modifier
) {
    GymNavHost(isUserRegistered = isUserRegistered(), modifier = modifier)
}

private fun isUserRegistered(): Boolean {
    //TODO: Get registration status from sqlite DB and validate with backend server
    return false
}

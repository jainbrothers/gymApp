package com.example.gymapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.flightsearchapp.ui.navigation.GymNavHost
import com.example.gymapp.ui.screens.choose.facilityApplication

@Composable
fun GymApp(
    modifier: Modifier = Modifier
) {
    GymNavHost(modifier = modifier)
}
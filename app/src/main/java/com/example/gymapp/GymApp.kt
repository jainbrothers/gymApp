package com.example.gymapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.flightsearchapp.ui.navigation.GymNavHost

@Composable
fun GymApp(
    modifier: Modifier = Modifier
) {
    GymNavHost(modifier = modifier)
}
package com.example.gymapp

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.gymapp.ui.navigation.GymNavHost

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun GymApp(
    modifier: Modifier = Modifier
) {
    GymNavHost(modifier = modifier)
}
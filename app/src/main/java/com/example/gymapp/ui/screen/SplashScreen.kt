package com.example.gymapp.ui.screen

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.gymapp.ui.screen.viewmodel.SplashScreenViewModel
import com.example.gymapp.ui.screen.viewmodel.enumeration.UserRegistrationState

private const val TAG = "Splash Screen"
@Composable
fun SplashScreen(
    unregisteredUserHandler: () -> Unit,
    registeredUserHandler: () -> Unit,
    otpVerifiedUserHandler: () -> Unit,
    modifier: Modifier = Modifier
) {
    val splashScreenViewModelviewModel: SplashScreenViewModel = hiltViewModel()
    val uiState by splashScreenViewModelviewModel.splashScreenUiState.collectAsState()
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (!uiState.isLoadingDone) {
            //TODO: Render loading image
            Text("App is initializing")
        } else {
            when(uiState.userRegistrationState) {
                UserRegistrationState.REGISTERED -> {
                    Text("Redirecting to home screen")
                    LaunchedEffect(uiState.userRegistrationState) {
                        registeredUserHandler()
                    }
                }
                UserRegistrationState.UNREGISTERED -> {
                    Text("Redirecting to registration screen")
                    LaunchedEffect(uiState.userRegistrationState) {
                        unregisteredUserHandler()
                    }
                }
                UserRegistrationState.OTP_VERICATION_SUCCESSFUL -> {
                    LaunchedEffect(uiState.userRegistrationState) {
                        otpVerifiedUserHandler()
                    }
                }
            }
        }
    }
}
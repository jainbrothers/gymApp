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
    navigateUnregisterUser: () -> Unit,
    navigateRegisterUser: () -> Unit,
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
            if (uiState.userRegistrationState.equals(UserRegistrationState.REGISTERED)) {
                Text("Redirecting to home screen")
                LaunchedEffect(uiState.userRegistrationState) {
                    navigateRegisterUser()
                }
            } else {
                Log.d(
                    TAG,
                    "uiState state ${uiState.userRegistrationState}, enum ${UserRegistrationState.REGISTERED}"
                )
                Text("Redirecting to registration screen")
                LaunchedEffect(uiState.userRegistrationState) {
                    navigateUnregisterUser()
                }
            }
        }
    }
}
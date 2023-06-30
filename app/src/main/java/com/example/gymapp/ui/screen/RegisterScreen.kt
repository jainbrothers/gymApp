package com.example.gymapp.ui.screen

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gymapp.ui.navigation.enum.ScreenName
import com.example.gymapp.ui.screens.choose.FacilitySearchApp

private const val TAG = "GYM APP LOG"
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onSuccessfulOTPGeneration: () -> Unit,
    onJumpHomeScreen: () -> Unit,
    modifier: Modifier = Modifier) {

    var text by remember { mutableStateOf(("")) }
    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        TextField(
            value = text,
            onValueChange = {
                //TODO: move the function to viewModel
                text = it
            },
            label = {Text("Enter mobile number")},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        var OTPGenerationError = null
        Button(
            onClick = {
                //TODO: call OTP generation func from viewModel and update OTPGenerationError
                if (OTPGenerationError == null) {
                    onSuccessfulOTPGeneration()
                }
            }
        ) {
            Text("Generate OTP")
        }

        if (OTPGenerationError != null) {
            Text("OTP generation failed with error ${OTPGenerationError}, please try again",
                modifier.align(CenterHorizontally))
        }
    }
    JumpToHomeScreen(onJumpHomeScreen)

}

@Composable
fun JumpToHomeScreen(onJumpHomeScreen: () -> Unit) {
    Button(onClick = onJumpHomeScreen) {
        Text("Tony!!! Jump to Home page")
    }
}
package com.example.gymapp.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gymapp.R
import com.example.gymapp.ui.screen.viewmodel.UserRegistrationViewModel

private const val TAG = "GYM APP LOG"
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserRegisterScreen(
    onSuccessfulOtpGeneration: () -> Unit,
    onSkipToHomePageButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val userRegistrationViewModel: UserRegistrationViewModel = hiltViewModel()
    val uiState by userRegistrationViewModel.userRegistrationUiState.collectAsState()
    Button(onClick = onSkipToHomePageButtonClick) {
        Text(text = "Click here to skip to home page")
    }
    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        TextField(
            value = uiState.mobileNumber,
            onValueChange = {
                userRegistrationViewModel.setMobileNumber(it)
            },
            label = {Text(stringResource(R.string.mobile_input_label))},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        var otpGenerationError: String ?= null
        Button(
            onClick = {
                otpGenerationError = userRegistrationViewModel.generateOtp(uiState.mobileNumber)
                if (otpGenerationError.isNullOrEmpty()) {
                    userRegistrationViewModel.saveUserMobileNumber()
                    onSuccessfulOtpGeneration()
                }
            },
            enabled = uiState.isOtpGenerationEnabled
        ) {
            Text(stringResource(R.string.generate_otp_button_name))
        }

        if (!otpGenerationError.isNullOrEmpty()) {
            Text("OTP generation failed with error ${otpGenerationError}, please try again",
                modifier.align(CenterHorizontally))
        }
    }
}

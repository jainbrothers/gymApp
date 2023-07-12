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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.gymapp.R
import com.example.gymapp.service.authservice.OtpVerificationState
import com.example.gymapp.ui.screen.enumeration.ErrorCode
import com.example.gymapp.ui.screen.viewmodel.OtpVerificationViewModel
import com.example.gymapp.ui.screen.viewmodel.enumeration.UserRegistrationState

private const val TAG = "Otp verification screen tag"
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OtpVerificationScreen(
    onSuccessfulOtpVerification: () -> Unit,
    modifier: Modifier = Modifier)
{
    val otpViewModel: OtpVerificationViewModel = hiltViewModel()
    val uiState by otpViewModel.otpVerificationUiState.collectAsState()
    val otpVerificationStatus by otpViewModel.otpVerificationStatus.collectAsState()
    if (otpVerificationStatus.otpVerificationState is OtpVerificationState.Successful) {
        LaunchedEffect(otpVerificationStatus.otpVerificationState) {
            otpViewModel.updateRegistrationState(UserRegistrationState.REGISTERED)
            onSuccessfulOtpVerification()
        }
    }
    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        Text("OTP sent to mobile number ${uiState.mobileNumber}")
        TextField(
            value = uiState.otp,
            onValueChange = {
                otpViewModel.setOtpValue(it)
            },
            label = {Text("Enter OTP received on your mobile")},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Button(
            onClick = {
                otpViewModel.verifyOtp(uiState.otp)
            },
            enabled = uiState.isVerifyOtpButtonEnabled
        ) {
            Text(stringResource(R.string.verify_otp_button_text))
        }
        if (uiState.otpVerificationError != ErrorCode.None) {
            Text("OTP verification failed with error ${uiState.otpVerificationError}, please try again",
                modifier.align(Alignment.CenterHorizontally))
        }
    }
}
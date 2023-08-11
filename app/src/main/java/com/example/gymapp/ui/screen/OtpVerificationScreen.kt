package com.example.gymapp.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.gymapp.R
import com.example.gymapp.service.authservice.OtpVerificationState
import com.example.gymapp.ui.screen.enumeration.ErrorCode
import com.example.gymapp.ui.screen.viewmodel.OTP_LENGTH
import com.example.gymapp.ui.screen.viewmodel.OtpVerificationViewModel
import com.example.gymapp.ui.screen.viewmodel.enumeration.UserRegistrationState
import com.example.gymapp.ui.utils.Spinner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "Otp verification screen tag"
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OtpVerificationScreen(
    onSuccessfulOtpVerification: () -> Unit,
    onClickChangeMobileNumberButton: () -> Unit,
    modifier: Modifier = Modifier)
{
    val otpViewModel: OtpVerificationViewModel = hiltViewModel()
    val uiState by otpViewModel.otpVerificationUiState.collectAsState()
//    val otpVerificationStatus by otpViewModel.otpVerificationStatus.collectAsState()
    val isOtpVerificationDone = (
            uiState.otpVerificationState is OtpVerificationState.Successful
                    || uiState.userRegistrationState == UserRegistrationState.OTP_VERICATION_SUCCESSFUL
            )
    if (isOtpVerificationDone) {
        LaunchedEffect(uiState.otpVerificationState) {
            otpViewModel.persistDetailsOfAuthenticatedUser()
        }
    }
    if (uiState.userRegistrationState == UserRegistrationState.REGISTERED) {
        LaunchedEffect(uiState.userRegistrationState) {
            onSuccessfulOtpVerification()
        }
    }
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        Text("OTP sent to mobile number ${uiState.mobileNumber}",
            modifier = modifier.padding(top = 50.dp)
        )
        TextField(
            value = uiState.otp,
            onValueChange = {
                otpViewModel.setOtpValue(it)
            },
            label = {Text("Enter OTP received on your mobile")},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = modifier.padding(50.dp)
        )
        if (uiState.authServiceErrorCode != ErrorCode.None) {
            Text("Error occurred during OTP verification ${uiState.authServiceErrorCode}",
                modifier.align(Alignment.CenterHorizontally))
        }
        if (uiState.errorCode != ErrorCode.None) {
            Text("Backgroud processing failure: ${uiState.errorCode}")
        }

        Box(modifier = modifier.fillMaxWidth().padding(50.dp),
            contentAlignment = Alignment.Center
        ){
            Button(
                onClick = {
                    CoroutineScope(Dispatchers.Default).launch {
                        otpViewModel.verifyOtp(uiState.otp)
                    }
                },
                enabled = uiState.otp.length == OTP_LENGTH && !uiState.isVerificationInProgress
            ) {
                Text(stringResource(R.string.verify_otp_button_text))
            }
            Spinner(doShow = uiState.isVerificationInProgress)
        }
        Button(
            onClick = {
                otpViewModel.updateStateForChangeMobileNumber()
                onClickChangeMobileNumberButton()
            },
            enabled = true,
            modifier = modifier.padding(20.dp)
        ) {
            Text("Change mobile number")
        }
    }
}
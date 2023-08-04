package com.example.gymapp.ui.screen

import android.util.Log
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
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.gymapp.R
import com.example.gymapp.service.authservice.OtpVerificationState
import com.example.gymapp.ui.screen.enumeration.ErrorCode
import com.example.gymapp.ui.screen.viewmodel.OtpVerificationViewModel
import com.example.gymapp.ui.screen.viewmodel.UserRegistrationViewModel
import com.example.gymapp.ui.screen.viewmodel.enumeration.UserRegistrationState
import com.example.gymapp.ui.utils.Spinner

private const val TAG = "GYM APP LOG"
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserRegisterScreen(
    onSuccessfulOtpGeneration: () -> Unit,
    postSuccessfulRegistration: () -> Unit,
    modifier: Modifier = Modifier
) {
    val userRegistrationViewModel: UserRegistrationViewModel = hiltViewModel()
    val otpVerificationViewModel: OtpVerificationViewModel = hiltViewModel()
    val otpVerificationStatus by userRegistrationViewModel.otpVerificationStatus.collectAsState()
    Log.d(TAG, "otpVerificationState ${otpVerificationStatus.otpVerificationState}")
    when(otpVerificationStatus.otpVerificationState) {
        is OtpVerificationState.NotInitialised,
        is OtpVerificationState.Error,
        is OtpVerificationState.Failed,
        is OtpVerificationState.OtpGenerationInProgress,
        is OtpVerificationState.ChangeMobileNumber -> GenerateOtp(modifier)
        is OtpVerificationState.OtpSent,
        is OtpVerificationState.OtpAutoFeedDone -> LaunchedEffect(otpVerificationStatus.otpVerificationState) {
            userRegistrationViewModel.saveUserMobileNumber()
            onSuccessfulOtpGeneration()
        }
        is OtpVerificationState.Successful -> LaunchedEffect(otpVerificationStatus.otpVerificationState) {
            otpVerificationViewModel.updateRegistrationState(UserRegistrationState.REGISTERED)
            postSuccessfulRegistration()
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenerateOtp(
    modifier: Modifier = Modifier
) {
    val userRegistrationViewModel: UserRegistrationViewModel = hiltViewModel()
    val uiState by userRegistrationViewModel.userRegistrationUiState.collectAsState()
    val otpVerificationStatus by userRegistrationViewModel.otpVerificationStatus.collectAsState()
    val enabled = uiState.isMobileNumberValid && !(otpVerificationStatus.otpVerificationState is OtpVerificationState.OtpGenerationInProgress)
    Log.d(TAG, "uiState.isOtpGenerationEnabled ${uiState.isGenerateOtpButtonEnabled} " +
            "isMobileValid ${uiState.isMobileNumberValid}")
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        TextField(
            value = uiState.mobileNumber,
            onValueChange = {
                userRegistrationViewModel.setMobileNumber(it)
            },
            label = {Text(stringResource(R.string.mobile_input_label))},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = modifier.padding(top = 100.dp, bottom = 50.dp)
        )
        Box(modifier = Modifier.fillMaxWidth().padding(50.dp), contentAlignment = Center) {
            Button(
                onClick = {
                    userRegistrationViewModel.generateOtp(uiState.mobileNumber)
                },
                enabled = enabled
            ) {
                Text(stringResource(R.string.generate_otp_button_name))
            }
            Spinner(doShow = otpVerificationStatus.otpVerificationState is OtpVerificationState.OtpGenerationInProgress)
        }
        if (otpVerificationStatus.errorCode != ErrorCode.None) {
            Text("OTP generation failed with error: ${otpVerificationStatus.errorCode}, please try again",
                modifier.align(CenterHorizontally).padding(50.dp))
        }
    }
}
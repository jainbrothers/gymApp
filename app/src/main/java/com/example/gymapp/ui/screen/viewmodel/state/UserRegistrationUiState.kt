package com.example.gymapp.ui.screen.viewmodel.state

import com.example.gymapp.service.authservice.OtpVerificationState
import com.example.gymapp.ui.screen.enumeration.ErrorCode
import com.example.gymapp.ui.screen.viewmodel.enumeration.UserRegistrationState

data class UserRegistrationUiState (
    val mobileNumber: String = "",
    val registrationState: UserRegistrationState = UserRegistrationState.UNREGISTERED,
    val isOtpGenerationEnabled: Boolean = false,
    val otpVerificationError: ErrorCode = ErrorCode.None
)
data class UserRegistrationExposedUiState (
    val mobileNumber: String = "",
    val registrationState: UserRegistrationState = UserRegistrationState.UNREGISTERED,
    val isOtpGenerationEnabled: Boolean = false,
    val signupState: OtpVerificationState = OtpVerificationState.NotInitialised
)
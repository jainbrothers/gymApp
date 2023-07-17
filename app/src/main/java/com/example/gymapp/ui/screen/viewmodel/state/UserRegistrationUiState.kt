package com.example.gymapp.ui.screen.viewmodel.state

import com.example.gymapp.service.authservice.OtpVerificationState
import com.example.gymapp.ui.screen.viewmodel.enumeration.UserRegistrationState

data class UserRegistrationUiState (
    val mobileNumber: String = "",
    val registrationState: UserRegistrationState = UserRegistrationState.UNREGISTERED,
    val isGenerateOtpButtonEnabled: Boolean = true,
    val isMobileNumberValid: Boolean = false
)
data class UserRegistrationExposedUiState (
    val mobileNumber: String = "",
    val registrationState: UserRegistrationState = UserRegistrationState.UNREGISTERED,
    val isOtpGenerationEnabled: Boolean = false,
    val signupState: OtpVerificationState = OtpVerificationState.NotInitialised
)
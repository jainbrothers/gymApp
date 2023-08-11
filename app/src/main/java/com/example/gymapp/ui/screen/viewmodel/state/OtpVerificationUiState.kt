package com.example.gymapp.ui.screen.viewmodel.state

import com.example.gymapp.service.authservice.OtpVerificationState
import com.example.gymapp.ui.screen.enumeration.ErrorCode
import com.example.gymapp.ui.screen.viewmodel.enumeration.UserRegistrationState

data class OtpVerificationUiState (
    val otp: String = "",
    val mobileNumber: String = "",
    val isVerificationInProgress: Boolean = false,
    val userRegistrationState: UserRegistrationState =  UserRegistrationState.UNREGISTERED,
    val authServiceErrorCode: ErrorCode = ErrorCode.None,
    val otpVerificationState: OtpVerificationState = OtpVerificationState.NotInitialised,
    val errorCode: ErrorCode = ErrorCode.None
)
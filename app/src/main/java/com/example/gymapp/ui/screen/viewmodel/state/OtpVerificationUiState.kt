package com.example.gymapp.ui.screen.viewmodel.state

import com.example.gymapp.ui.screen.enumeration.ErrorCode

data class OtpVerificationUiState (
    val otp: String = "",
    val isVerifyOtpButtonEnabled: Boolean = false,
    val mobileNumber: String = "",
    val otpVerificationError: ErrorCode = ErrorCode.None
)
package com.example.gymapp.ui.screen.viewmodel.state

data class OtpVerificationUiState (
    val otp: String = "",
    val isVerifyOtpButtonEnabled: Boolean = false,
    val mobileNumber: String = ""
)
package com.example.gymapp.ui.screen.viewmodel.state

import com.example.gymapp.service.authservice.OtpVerificationState

data class OtpVerificationUiState (
    val otp: String = "",
    val mobileNumber: String = "",
    val isVerificationInProgress: Boolean = false
)
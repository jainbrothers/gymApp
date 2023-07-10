package com.example.gymapp.ui.screen.viewmodel.state

import com.example.gymapp.ui.screen.viewmodel.enumeration.UserRegistrationState

data class UserRegistrationUiState (
    val mobileNumber: String = "",
    val registrationState: UserRegistrationState = UserRegistrationState.UNREGISTERED,
    val isOtpGenerationEnabled: Boolean = false
)
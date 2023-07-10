package com.example.gymapp.ui.screen.viewmodel.state

import com.example.gymapp.ui.screen.viewmodel.enumeration.UserRegistrationState

data class SplashScreenUiState (
    val userRegistrationState: UserRegistrationState = UserRegistrationState.UNREGISTERED,
    val isLoadingDone: Boolean = false
)
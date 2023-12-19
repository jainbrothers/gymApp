package com.example.gymapp.ui.screen.viewmodel.state

import com.example.gymapp.model.Gym
import com.example.gymapp.ui.screen.enumeration.ErrorCode

data class BookSessionConfirmationUiState (
    val gym: Gym? = null,
    val sessionStartEpochInMilli: Long? = null,
    val durationInMinute: Int? = null,
    var errorCode: ErrorCode = ErrorCode.None
)
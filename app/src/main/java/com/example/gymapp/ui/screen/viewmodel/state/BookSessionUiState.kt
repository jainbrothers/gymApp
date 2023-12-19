package com.example.gymapp.ui.screen.viewmodel.state

import com.example.gymapp.application.DEFAULT_ACTIVITY_FOR_SESSION_SCHEDULE_LISTING
import com.example.gymapp.model.BookingSessionDetail
import com.example.gymapp.model.Gym
import com.example.gymapp.model.SessionTiming
import com.example.gymapp.ui.screen.enumeration.ErrorCode

data class BookSessionUiState(
    val gym: Gym? = null,
    val errorCode: ErrorCode = ErrorCode.None,
    val daywiseSessionSchedule: List<DaywiseSessionSchedule>? = listOf(),
    val selectedActivity: String = DEFAULT_ACTIVITY_FOR_SESSION_SCHEDULE_LISTING,
    val selectedSessionInfo: SelectedSessionInfo? = null,
    val bookingSessionDetail: BookingSessionDetail? = null
)
data class SelectedSessionInfo(
    val sessionIndexInSchedule: Int,
    val tabIndexOfSchedule: Int,
    val sessionTiming: SessionTiming
)
data class DaywiseSessionSchedule(
    val sessionTimings: List<SessionTiming>?,
    val displayName: String
)
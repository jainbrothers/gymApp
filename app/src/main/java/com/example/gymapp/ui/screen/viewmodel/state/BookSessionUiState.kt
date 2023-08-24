package com.example.gymapp.ui.screen.viewmodel.state

import com.example.gymapp.application.DEFAULT_ACTIVTY_FOR_SCHEDULE_LISTING
import com.example.gymapp.model.Gym
import com.example.gymapp.model.SessionSchedule
import com.example.gymapp.ui.screen.enumeration.ErrorCode

data class BookSessionUiState(
    val gym: Gym? = null,
    val errorCode: ErrorCode = ErrorCode.None,
    val selectedSessionScheduleIndex: Int ?= null,
    val pageIndexOfSelectedSchedule: Int ?= null,
    val scheduleList: List<Pair<String, List<SessionSchedule>?>>? = listOf(),
    val selectedActivity: String = DEFAULT_ACTIVTY_FOR_SCHEDULE_LISTING
)
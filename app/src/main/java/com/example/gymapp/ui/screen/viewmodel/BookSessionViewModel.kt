package com.example.gymapp.ui.screen.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymapp.application.SCHEDULE_DISPLAY_FOR_DAYS
import com.example.gymapp.data.repository.gym.GymRepository
import com.example.gymapp.model.Gym
import com.example.gymapp.model.SessionSchedule
import com.example.gymapp.ui.navigation.GYM_ID_ARGUMENT_NAME
import com.example.gymapp.ui.screen.enumeration.ErrorCode
import com.example.gymapp.ui.screen.viewmodel.state.BookSessionUiState
import com.example.gymapp.util.DAYS_NAME
import com.example.gymapp.util.DAY_INDEX_TO_DISPLAY_NAME
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import okhttp3.internal.toImmutableList
import java.util.Calendar
import java.util.Calendar.MONTH
import java.util.Calendar.SHORT
import java.util.Locale
import javax.inject.Inject

private val TAG = BookSessionViewModel::class.java.simpleName
@HiltViewModel
class BookSessionViewModel @Inject constructor(
    val savedStateHandle: SavedStateHandle, val gymRepository: GymRepository,
) : ViewModel() {
    private val gymId: String = checkNotNull(savedStateHandle[GYM_ID_ARGUMENT_NAME])
    private val _bookSessionUistate = MutableStateFlow(BookSessionUiState())
    private var gym: Gym? = null
    private var errorCode: ErrorCode = ErrorCode.None
    val bookSessionUiState: StateFlow<BookSessionUiState> = _bookSessionUistate.map { uiState ->
        gym = gymRepository.getGymById(gymId) ?: Gym()
        errorCode = uiState.errorCode
        if (gym == null) {
            errorCode =
                ErrorCode.InternalServiceError("Selected Gym record not found. Reference = ${gymId}")
        }
        val scheduleListForDisplay: List<Pair<String, List<SessionSchedule>?>>? =
            gym
                ?.dayWiseScheduleListForActivity
                ?.let {
                    retrieveScheduleForUserDisplay(it)
                }
        BookSessionUiState(
            gym = gym,
            errorCode = errorCode,
            selectedSessionScheduleIndex = uiState.selectedSessionScheduleIndex,
            pageIndexOfSelectedSchedule = uiState.pageIndexOfSelectedSchedule,
            scheduleList = scheduleListForDisplay,
            selectedActivity = uiState.selectedActivity
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        (BookSessionUiState())
    )

    fun setSelectedSessionScheduleIndex(
        selectedSessionScheduleIndex: Int,
        pageIndexOfSelectedSchedule: Int
    ) {
        _bookSessionUistate.update { currentState ->
            currentState.copy(
                selectedSessionScheduleIndex = selectedSessionScheduleIndex,
                pageIndexOfSelectedSchedule = pageIndexOfSelectedSchedule
            )
        }
    }
    private fun retrieveScheduleForUserDisplay(
        scheduleList: Map<String, Map<String, List<SessionSchedule>>>
    ): List<Pair<String, List<SessionSchedule>?>> {
        val calendar = Calendar.getInstance()
        val today = calendar[Calendar.DAY_OF_WEEK] - 1
        val activityToScheduleListForDisplay: MutableList<Pair<String, List<SessionSchedule>?>> = mutableListOf()
        for (dayIndex in 0..SCHEDULE_DISPLAY_FOR_DAYS - 1) {
            val scheduleListForActivity = (scheduleList[bookSessionUiState.value.selectedActivity]?.let {
                    retrieveScheduleForActivity(it, DAYS_NAME[today + dayIndex])
                })
            val displayName: String = getDisplayName(dayIndex)
            activityToScheduleListForDisplay.add(Pair(displayName, scheduleListForActivity))
        }
        return activityToScheduleListForDisplay.toImmutableList()
    }
    private fun getDisplayName(dayIndex: Int): String {
        var displayName: String? = DAY_INDEX_TO_DISPLAY_NAME.get(dayIndex)
        if (displayName == null) {
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_YEAR, dayIndex)
            displayName = "${calendar.get(Calendar.DAY_OF_MONTH)} ${calendar.getDisplayName(MONTH, SHORT, Locale.ENGLISH)}"
        }
        return displayName
    }

    private fun retrieveScheduleForActivity(
        scheduleList: Map<String, List<SessionSchedule>>,
        dayName: String
    ): List<SessionSchedule>? {
        return scheduleList?.get(dayName)
    }
}
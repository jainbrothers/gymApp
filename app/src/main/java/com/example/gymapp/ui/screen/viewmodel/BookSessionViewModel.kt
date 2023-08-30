package com.example.gymapp.ui.screen.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymapp.application.SCHEDULE_DISPLAY_FOR_DAYS
import com.example.gymapp.data.repository.gym.GymRepository
import com.example.gymapp.model.Gym
import com.example.gymapp.model.SessionTiming
import com.example.gymapp.ui.navigation.GYM_ID_ARGUMENT_NAME
import com.example.gymapp.ui.screen.enumeration.ErrorCode
import com.example.gymapp.ui.screen.viewmodel.state.BookSessionUiState
import com.example.gymapp.ui.screen.viewmodel.state.DaywiseSessionSchedule
import com.example.gymapp.ui.screen.viewmodel.state.SelectedSessionInfo
import com.example.gymapp.util.DAYS_NAME
import com.example.gymapp.util.DAY_INDEX_TO_DISPLAY_NAME
import com.example.gymapp.util.NUM_OF_DAYS_IN_WEEK
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.internal.toImmutableList
import java.util.Calendar
import java.util.Calendar.MONTH
import java.util.Calendar.SHORT
import java.util.Locale
import javax.inject.Inject

private val TAG = BookSessionViewModel::class.java.simpleName
@HiltViewModel
class BookSessionViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle, val gymRepository: GymRepository,
) : ViewModel() {
    private val gymId: String = checkNotNull(savedStateHandle[GYM_ID_ARGUMENT_NAME])
    private val _bookSessionUistate = MutableStateFlow(BookSessionUiState())
    private var gymFlow: Flow<Gym?>? = null
    private var errorCode: ErrorCode = ErrorCode.None
    init {
        viewModelScope.launch {
            gymFlow = gymRepository.getGymById(gymId)
        }
    }
    val bookSessionUiState: StateFlow<BookSessionUiState> = gymFlow!!.combine(_bookSessionUistate) {gym, uiState ->
            errorCode = uiState.errorCode
            if (gym == null) {
                errorCode =
                    ErrorCode.InternalServiceError("Selected Gym record not found. Reference = ${gymId}")
            }
        BookSessionUiState(
            gym = gym,
            errorCode = errorCode,
            selectedSessionInfo = uiState.selectedSessionInfo,
            daywiseSessionSchedule = mapSessionScheduleToDisplayName(gym) as List<DaywiseSessionSchedule>?,
            selectedActivity = uiState.selectedActivity
        )
    }
    .stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        (BookSessionUiState())
    )
    fun setSelectedSessionScheduleIndex(
        selectedSessionInfo: SelectedSessionInfo
    ) {
        _bookSessionUistate.update { currentState ->
            currentState.copy(
                selectedSessionInfo = selectedSessionInfo
            )
        }
    }
    private fun mapSessionScheduleToDisplayName(gym: Gym?
    ): List<DaywiseSessionSchedule?>? {
        if (gym == null || gym.activityToDayToSessionScheduleMap.isNullOrEmpty()) {
            return null
        }
        val calendar = Calendar.getInstance()
        val today = calendar[Calendar.DAY_OF_WEEK] - 1
        var dayWiseSessionSchedule: MutableList<DaywiseSessionSchedule?> = mutableListOf()
        for (dayIndex in 0..SCHEDULE_DISPLAY_FOR_DAYS - 1) {
            val sessionSchedule = (gym.activityToDayToSessionScheduleMap[_bookSessionUistate.value.selectedActivity]?.let
            {
                retrieveScheduleForActivity(it, DAYS_NAME[(today + dayIndex) % NUM_OF_DAYS_IN_WEEK])
            })
            val displayName: String = getDisplayName(dayIndex)
            dayWiseSessionSchedule.add(DaywiseSessionSchedule(sessionSchedule?.toImmutableList(), displayName))
        }
        return dayWiseSessionSchedule.toImmutableList()
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
        scheduleList: Map<String, List<SessionTiming>>,
        dayName: String
    ): List<SessionTiming>? {
        return scheduleList?.get(dayName)
    }
}
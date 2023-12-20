package com.example.gymapp.ui.screen.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymapp.data.repository.gym.GymRepository
import com.example.gymapp.model.Gym
import com.example.gymapp.ui.navigation.DURATION_IN_MINUTE_ARGUMENT_NAME
import com.example.gymapp.ui.navigation.GYM_ID_ARGUMENT_NAME
import com.example.gymapp.ui.navigation.SESSION_START_EPOCH_IN_MILLI_ARGUMENT_NAME
import com.example.gymapp.ui.screen.enumeration.ErrorCode
import com.example.gymapp.ui.screen.viewmodel.state.BookSessionConfirmationUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private val TAG = BookSessionConfirmationViewModel::class.java.simpleName
@HiltViewModel
class BookSessionConfirmationViewModel @Inject constructor(
    val savedStateHandle: SavedStateHandle,
    val gymRepository: GymRepository
) : ViewModel() {
    private val gymId: String? = savedStateHandle[GYM_ID_ARGUMENT_NAME]
    private val durationInMinute: Int? = savedStateHandle[DURATION_IN_MINUTE_ARGUMENT_NAME]
    private val sessionStartEpochInMilli: Long? = savedStateHandle[SESSION_START_EPOCH_IN_MILLI_ARGUMENT_NAME]
    private var gymFlow: Flow<Gym?>? = null
    private var errorCode: ErrorCode = ErrorCode.None
    private val _bookSessionConfirmationUiState = MutableStateFlow(BookSessionConfirmationUiState(errorCode = errorCode))
    init {
        viewModelScope.launch {
            initialise()
        }
    }
    val bookSessionConfirmationUiState: StateFlow<BookSessionConfirmationUiState> = createUiState()
    private suspend fun initialise() {
        try {
            checkNotNull(gymId)
            checkNotNull(durationInMinute)
            checkNotNull(sessionStartEpochInMilli)
        } catch (exception:  IllegalStateException) {
            errorCode = ErrorCode.InternalClientException("Unexpected null value of agrument", exception)
        }
        try {
            gymFlow = gymRepository.getGymById(gymId!!)
        } catch (exception: ErrorCode) {
            errorCode = exception
        }
        _bookSessionConfirmationUiState.update { uiState ->
            uiState.copy(
                errorCode = errorCode,
                sessionStartEpochInMilli = sessionStartEpochInMilli,
                durationInMinute = durationInMinute
            )
        }
    }
    private fun createUiState():StateFlow<BookSessionConfirmationUiState> {
        if (gymFlow == null) {
            errorCode = ErrorCode.InternalClientError("Error while fetching gym details")
        }
        if (errorCode != ErrorCode.None) {
            _bookSessionConfirmationUiState.update {uiState ->
                uiState.copy(errorCode = errorCode)
            }
            return _bookSessionConfirmationUiState.asStateFlow()
        } else {
            return gymFlow!!.filterNotNull().map { gym ->
                errorCode = _bookSessionConfirmationUiState.value.errorCode
                BookSessionConfirmationUiState(
                    gym = gym,
                    errorCode = errorCode,
                    sessionStartEpochInMilli = _bookSessionConfirmationUiState.value.sessionStartEpochInMilli,
                    durationInMinute = _bookSessionConfirmationUiState.value.durationInMinute
                )
            }
                .stateIn(
                    viewModelScope,
                    SharingStarted.WhileSubscribed(5000L),
                    (BookSessionConfirmationUiState(errorCode = ErrorCode.DELAYED_LOADING))
                )
        }
    }
}
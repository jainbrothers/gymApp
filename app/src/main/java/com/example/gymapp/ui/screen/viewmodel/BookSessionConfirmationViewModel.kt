package com.example.gymapp.ui.screen.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymapp.data.repository.gym.GymRepository
import com.example.gymapp.model.BookingSessionDetail
import com.example.gymapp.model.Gym
import com.example.gymapp.ui.screen.enumeration.ErrorCode
import com.example.gymapp.ui.screen.viewmodel.state.BookSessionConfirmationUiState
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.lang.RuntimeException
import javax.inject.Inject

private val TAG = BookSessionConfirmationViewModel::class.java.simpleName
@HiltViewModel
class BookSessionConfirmationViewModel @Inject constructor(
    val savedStateHandle: SavedStateHandle,
    val gymRepository: GymRepository
) : ViewModel() {
    private var gymFlow: Flow<Gym?>? = null
    private lateinit var bookingSessionDetail: BookingSessionDetail
    private var errorCode: ErrorCode = ErrorCode.None
    private val _bookSessionConfirmationUiState = MutableStateFlow(BookSessionConfirmationUiState(errorCode = errorCode))
    lateinit var bookSessionConfirmationUiState: StateFlow<BookSessionConfirmationUiState>
    private set

    init {
        viewModelScope.launch {
            initialise()
        }
    }
    private suspend fun initialise() {
        var bookingSessionDetailJson: String? = null
        try {
            bookingSessionDetailJson = requireNotNull(savedStateHandle["bookSessionDetail"])
        } catch (exception: IllegalArgumentException) {
            errorCode = ErrorCode.InternalClientException("Missing param bookSessionDetail", exception)
        }
        if (errorCode != ErrorCode.None) {
            return
        }
        lateinit var bookingSessionDetailInput: BookingSessionDetail
        try {
            bookingSessionDetailInput = Gson()
                .fromJson(
                    Uri.decode(bookingSessionDetailJson),
                    BookingSessionDetail::class.java
                )
        } catch (exception: JsonSyntaxException) {
            errorCode = ErrorCode.InternalClientException("Json decoding error. Json = ${bookingSessionDetailJson}", exception)
        } catch (exception: Exception) {
            errorCode = ErrorCode.InternalClientException("Unexpected error occurred.", exception)
        }
        Log.d(TAG, "bookingSessionDetailInput ${bookingSessionDetailInput}")
        if (errorCode != ErrorCode.None) {
            _bookSessionConfirmationUiState.update { state ->
                state.copy(errorCode = errorCode)
            }
            bookSessionConfirmationUiState = _bookSessionConfirmationUiState.map { uiState ->
                BookSessionConfirmationUiState(
                    errorCode = errorCode
                )
            }.stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000L),
                (BookSessionConfirmationUiState())
            )
        } else {
            bookingSessionDetail = bookingSessionDetailInput
            _bookSessionConfirmationUiState.update { state ->
                state.copy(
                    sessionStartTimestamp = bookingSessionDetail.sessionStartTimestamp,
                    durationInMinute = bookingSessionDetail.durationInMinute,
                    errorCode = errorCode
                )
            }
            gymFlow = gymRepository.getGymById(bookingSessionDetail.gymId)
            bookSessionConfirmationUiState = gymFlow!!.map { gym ->
                errorCode = _bookSessionConfirmationUiState.value.errorCode
                Log.d(TAG, "gym = ${gym}")
                if (gym == null) {
                    errorCode =
                        ErrorCode.InternalServiceError("Selected Gym record not found. Reference = ${bookingSessionDetail.gymId}")
                }
                BookSessionConfirmationUiState(
                    gym = gym,
                    errorCode = errorCode,
                    sessionStartTimestamp = _bookSessionConfirmationUiState.value.sessionStartTimestamp,
                    durationInMinute = _bookSessionConfirmationUiState.value.durationInMinute
                )
            }
                .stateIn(
                    viewModelScope,
                    SharingStarted.WhileSubscribed(5000L),
                    (BookSessionConfirmationUiState())
                )
        }
    }
}
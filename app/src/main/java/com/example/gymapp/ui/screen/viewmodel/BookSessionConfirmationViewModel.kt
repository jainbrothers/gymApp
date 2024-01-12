package com.example.gymapp.ui.screen.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymapp.data.repository.UserDetailRepository
import com.example.gymapp.data.repository.gym.GymRepository
import com.example.gymapp.data.repository.session.BookedSessionRepository
import com.example.gymapp.data.repository.user.UserRepository
import com.example.gymapp.model.Gym
import com.example.gymapp.model.session.BookedSessionDetail
import com.example.gymapp.model.session.SessionStatus
import com.example.gymapp.ui.navigation.ACTIVITY_ID_ARGUMENT_NAME
import com.example.gymapp.ui.navigation.DURATION_IN_MINUTE_ARGUMENT_NAME
import com.example.gymapp.ui.navigation.GYM_ID_ARGUMENT_NAME
import com.example.gymapp.ui.navigation.SESSION_START_EPOCH_IN_MILLI_ARGUMENT_NAME
import com.example.gymapp.ui.screen.enumeration.ErrorCode
import com.example.gymapp.ui.screen.viewmodel.state.BookSessionConfirmationUiState
import com.example.gymapp.util.UNDERSCORE_STRING
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.lang.StringBuilder
import java.time.Instant
import javax.inject.Inject

private val TAG = BookSessionConfirmationViewModel::class.java.simpleName
@HiltViewModel
class BookSessionConfirmationViewModel @Inject constructor(
    val savedStateHandle: SavedStateHandle,
    val gymRepository: GymRepository,
    val bookedSessionRepository: BookedSessionRepository,
    val userDetailRepository: UserDetailRepository,
    val userRepository: UserRepository
) : ViewModel() {
    private val gymId: String? = savedStateHandle[GYM_ID_ARGUMENT_NAME]
    private val activityId: String? = savedStateHandle[ACTIVITY_ID_ARGUMENT_NAME]
    private val durationInMinute: Int? = savedStateHandle[DURATION_IN_MINUTE_ARGUMENT_NAME]
    private val sessionStartEpochInMilli: Long? = savedStateHandle[SESSION_START_EPOCH_IN_MILLI_ARGUMENT_NAME]
    private var gymFlow: Flow<Gym?>? = null
    private lateinit var userId: String
    private var errorCode: ErrorCode = ErrorCode.None
    private val _bookSessionConfirmationUiState = MutableStateFlow(BookSessionConfirmationUiState(errorCode = errorCode))
    init {
        viewModelScope.launch {
            validateInput()
            getGym()
            populateUserId()
            validateUser()
            _bookSessionConfirmationUiState.update { uiState ->
                uiState.copy(
                    errorCode = errorCode,
                    sessionStartEpochInMilli = sessionStartEpochInMilli,
                    durationInMinute = durationInMinute
                )
            }
        }
    }

    val bookSessionConfirmationUiState: StateFlow<BookSessionConfirmationUiState> = createUiState()
    private fun validateInput() {
        try {
            checkNotNull(gymId)
            checkNotNull(durationInMinute)
            checkNotNull(sessionStartEpochInMilli)
        } catch (exception:  IllegalStateException) {
            errorCode = ErrorCode.InternalClientException("Unexpected null value of agrument", exception)
        }
    }
    private suspend fun getGym() {
        try {
            gymFlow = gymRepository.getGymById(gymId!!)
        } catch (exception: ErrorCode) {
            errorCode = exception
        }
    }
    private suspend fun populateUserId() {
        userId = userDetailRepository.userId.first().toString()
        if (userId == null) {
            errorCode = ErrorCode.InternalServiceError("null value for userId found.")
        }
    }
    private suspend fun validateUser() {
        if (errorCode != ErrorCode.None || errorCode != ErrorCode.DELAYED_LOADING) {
            return
        }
        try {
            userRepository.getbyId(userId!!).collect { user ->
                if (user == null) {
                    Log.e(TAG, "User not found in DB for user Id $userId")
                    errorCode = ErrorCode.InternalServiceError("User not found for user Id = $userId")

                } else {
                    if (user!!.mobileNumber != userDetailRepository.userMobileNumber.first()) {
                        Log.e(TAG, "Local and DB mobile number mismatch for user id = $userId, DB mobile number ${user!!.mobileNumber}, local mobile number ${userDetailRepository.userMobileNumber.first()}")
                        errorCode = ErrorCode.InternalClientError("User validation failed due to mismatch in mobile number.")
                    }
                }
            }
        } catch (exception: ErrorCode) {
            errorCode = exception
        } catch (exception: Exception) {
            Log.e(TAG, "Unexpected error during validation of user $exception")
            errorCode = ErrorCode.InternalServiceException("Unexpected error during user validation.", exception)
        }
    }
    private fun createUiState():StateFlow<BookSessionConfirmationUiState> {
        if (gymFlow == null) {
            Log.e(TAG, "Gymflow is null")
            errorCode = ErrorCode.InternalClientError("Error while fetching gym details")
        }
        if (errorCode != ErrorCode.None) {
            Log.e(TAG, "Error occurred ${errorCode}")
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
                (
                    BookSessionConfirmationUiState(
                        errorCode = ErrorCode.DELAYED_LOADING,
                        sessionStartEpochInMilli = sessionStartEpochInMilli,
                        durationInMinute = durationInMinute
                    )
                )
            )
        }
    }
    fun createBookedSessionDetail() {
        viewModelScope.launch {
            val bookedSessionDetail = buildBookedSessionDetail()
            bookedSessionRepository.create(
                bookedSessionDetail,
                StringBuilder()
                    .append(userId)
                    .append(UNDERSCORE_STRING)
                    .append(bookedSessionDetail.sessionStartTimeEpochInMillis)
                    .toString()
            )
        }
    }
    fun buildBookedSessionDetail(): BookedSessionDetail {
        return BookedSessionDetail(
            activityId = activityId!!,
            durationInMinute = durationInMinute!!,
            gymId = gymId!!,
            sessionStartTimeEpochInMillis = sessionStartEpochInMilli!!,
            bookingTimeEpochInMillis = Instant.now().toEpochMilli(),
            status = SessionStatus.BOOKED,
            paymentDetail = null
        )
    }
}
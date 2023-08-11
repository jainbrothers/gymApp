package com.example.gymapp.ui.screen.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymapp.data.repository.UserDetailRepository
import com.example.gymapp.data.repository.user.UserRepository
import com.example.gymapp.data.repository.user.UserRepositoryException
import com.example.gymapp.model.User
import com.example.gymapp.service.authservice.AuthService
import com.example.gymapp.service.authservice.OtpVerificationStatus
import com.example.gymapp.ui.screen.viewmodel.enumeration.UserRegistrationState
import com.example.gymapp.ui.screen.viewmodel.state.OtpVerificationUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject


private const val TAG = "Otp View tag"

@HiltViewModel
class OtpVerificationViewModel @Inject constructor(
    val userDetailRepository: UserDetailRepository,
    val authService: AuthService,
    val userRepository: UserRepository
): ViewModel()
{
    private val _otpVerificationUiState = MutableStateFlow(OtpVerificationUiState())
    val otpVerificationUiState = _otpVerificationUiState.asStateFlow()
    val otpVerificationStatus: StateFlow<OtpVerificationStatus> = authService.otpVerificationStatus.asStateFlow()
    init {
        populateUserDetail()
    }
    private fun populateUserDetail() {
        viewModelScope.launch {
            userDetailRepository.userRegistrationStatus.collect { userRegistrationState ->
                userDetailRepository.userMobileNumber.collect { mobileNumber ->
                    _otpVerificationUiState.update { currentState ->
                        currentState.copy(
                            userRegistrationState = userRegistrationState,
                            mobileNumber = mobileNumber
                        )
                    }
                }
            }
        }
    }

    suspend fun verifyOtp(otp: String) {
        viewModelScope.launch {
            Log.d(TAG, "inside verifyOtp function, otp = ${otp}")
            _otpVerificationUiState.update {currentState ->
                currentState.copy(
                    isVerificationInProgress = true
                )
            }
            authService.verifyOtp(otp)
            _otpVerificationUiState.update {currentState ->
                currentState.copy(
                    isVerificationInProgress = false
                )
            }
        }
    }
    fun setOtpValue(userInput: String) {
        if (isOtpValid(userInput)) {
            _otpVerificationUiState.update { stateCurrentValue ->
                stateCurrentValue.copy(
                    otp = userInput
                )
            }
        }
    }
    fun updateStateForChangeMobileNumber() {
        authService.updateStateForChangeMobileNumber()
    }

    private fun isOtpValid(otp: String): Boolean {
        var valid = otp.isNullOrEmpty()
        valid = valid || otp.matches(NUMBER_PATTERN)
        valid = valid && otp.length <= OTP_LENGTH
        return valid
    }

    suspend fun persistDetailsOfAuthenticatedUser() {
        Log.d(TAG, "RRD entered persistDetailsOfAuthenticatedUser")
        viewModelScope.launch {
            Log.d(TAG, "RRD entered persistDetailsOfAuthenticatedUser inside launched scope")
            try {
                userRepository.getbyMobileNumber(otpVerificationUiState.value.mobileNumber, ::persistUserDetails)
            } catch(e: UserRepositoryException.UserNotFound) {
                persistUserDetails(null)
            }
        }
    }
    fun persistUserDetails(userRecord: User?) {
        CoroutineScope(Dispatchers.Default).launch {
            Log.d(TAG, "persistUserDetails entered userRecord ${userRecord}")
            var user = userRecord
            try {
                if (user == null) {
                    user  = createUser()
                }
                userDetailRepository.saveUserId(user.userId)
                userDetailRepository.saveUserRegistrationState(UserRegistrationState.REGISTERED)
            } catch(e: RuntimeException) {
                //TODO Error handling. set errorcode to render on UI
            }
        }
    }
    private suspend fun createUser(): User {
        val user = User(
            mobileNumber = otpVerificationUiState.value.mobileNumber,
            userId = UUID.randomUUID().toString()
        )
        userRepository.create(user)
        return user
    }
}
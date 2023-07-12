package com.example.gymapp.ui.screen.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymapp.application.COUNTRY_CODE
import com.example.gymapp.data.repository.UserDetailRepository
import com.example.gymapp.service.authservice.AuthService
import com.example.gymapp.service.authservice.OtpVerificationState
import com.example.gymapp.service.authservice.OtpVerificationStatus
import com.example.gymapp.ui.screen.enumeration.ErrorCode
import com.example.gymapp.ui.screen.viewmodel.enumeration.UserRegistrationState
import com.example.gymapp.ui.screen.viewmodel.state.UserRegistrationUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private val TAG = "User Registration View Model"

@HiltViewModel
class UserRegistrationViewModel @Inject constructor(
    val userDetailRepository: UserDetailRepository,
    val authService: AuthService
): ViewModel()
{
    private val _userRegistrationUiState = MutableStateFlow(UserRegistrationUiState())
    val userRegistrationUiState: StateFlow<UserRegistrationUiState>  = _userRegistrationUiState.asStateFlow()
    val otpVerificationStatus: StateFlow<OtpVerificationStatus> = authService.otpVerificationStatus.asStateFlow()

//    val exposedStateFlow = userRegistrationUiState.combine(signupStatus)
//    { userRegistrationUiState1, signupStatus1 ->
//        UserRegistrationExposedUiState(
//            userRegistrationUiState1.mobileNumber,
//            userRegistrationUiState1.registrationState,
//            userRegistrationUiState1.isOtpGenerationEnabled,
//            signupStatus1
//        )
//    }
    init {
        populateRegistrationUiState()
    }

    private fun populateRegistrationUiState() {
        viewModelScope.launch {
            userDetailRepository.userMobileNumber.collect {mobileNumber ->
                _userRegistrationUiState.update {currentState ->
                    currentState.copy(mobileNumber = mobileNumber,
                        isOtpGenerationEnabled = isOtpGenerationEnabled(mobileNumber))
                }
            }
            userDetailRepository.userRegistrationStatus.collect { registrationStatus ->
                _userRegistrationUiState.update { currentState ->
                    currentState.copy(registrationState = registrationStatus)
                }
            }
            Log.d(TAG, "datastore mobile # ${_userRegistrationUiState.value.mobileNumber}")
            Log.d(TAG, "datastore registration # ${_userRegistrationUiState.value.registrationState}")
        }
    }

    fun setMobileNumber(userInput: String) {
        if (isMobileNumberValid(userInput)) {
            _userRegistrationUiState.update { currentState ->
                currentState.copy(
                    mobileNumber = userInput,
                    isOtpGenerationEnabled = isOtpGenerationEnabled(userInput)
                )
            }
        }
    }
    fun generateOtp(mobileNumber: String) {
        Log.d(TAG, "calling authService.authenticate")
        authService.authenticate(COUNTRY_CODE + mobileNumber)
    }

    fun saveUserMobileNumber() {
         viewModelScope.launch {
             userDetailRepository.saveUserMobileNumber(userRegistrationUiState.value.mobileNumber)
         }
    }

    private fun isMobileNumberValid(mobileNumber: String): Boolean {
        var valid = mobileNumber.isNullOrEmpty()
        valid = valid || mobileNumber.matches(NUMBER_PATTERN)
        valid = valid && mobileNumber.length <= MOBILE_NUMBER_LENGTH
        return valid
    }

    private fun isOtpGenerationEnabled(mobileNumber: String): Boolean {
        return mobileNumber.length == MOBILE_NUMBER_LENGTH
    }
}
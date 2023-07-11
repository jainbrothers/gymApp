package com.example.gymapp.ui.screen.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymapp.data.repository.UserDetailRepository
import com.example.gymapp.ui.screen.viewmodel.enumeration.OtpVerificationCode
import com.example.gymapp.ui.screen.viewmodel.enumeration.UserRegistrationState
import com.example.gymapp.ui.screen.viewmodel.state.OtpVerificationUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


private const val TAG = "Otp View tag"

@HiltViewModel
class OtpVerificationViewModel @Inject constructor(val userDetailRepository: UserDetailRepository): ViewModel() {

    private val _otpVerificationUiState = MutableStateFlow(OtpVerificationUiState())
    val otpVerificationUiState = _otpVerificationUiState.asStateFlow()
    init {
        populateUserDetail()
    }
    private fun populateUserDetail() {
        viewModelScope.launch {
            userDetailRepository.userMobileNumber.collect {mobileNumber ->
                _otpVerificationUiState.update {currentState ->
                    currentState.copy(mobileNumber = mobileNumber)
                }
            }
        }
    }

    fun verifyOtp(otp: String): OtpVerificationCode {
        //TODO: call backend server to verify otp
        Log.d(TAG, "inside verifyOtp function, otp = ${otp}")
        updateRegistrationState(UserRegistrationState.REGISTERED)
        return OtpVerificationCode.SUCCEEDED
    }

    private fun updateRegistrationState(state: UserRegistrationState) {
        viewModelScope.launch {
            Log.d(TAG, "updating registration ${state}")
            userDetailRepository.saveUserRegistrationState(state)
        }
    }

    fun setOtpValue(userInput: String) {
        if (isOtpValid(userInput)) {
            _otpVerificationUiState.update { stateCurrentValue ->
                stateCurrentValue.copy(
                    otp = userInput,
                    isVerifyOtpButtonEnabled = isVerifyOtpButtonEnabled(userInput)
                )
            }
        }
    }

    private fun isOtpValid(otp: String): Boolean {
        var valid = otp.isNullOrEmpty()
        valid = valid || otp.matches(NUMBER_PATTERN)
        valid = valid && otp.length <= OTP_LENGTH
        return valid
    }

    private fun isVerifyOtpButtonEnabled(userInput: String) : Boolean {
        return userInput.length == OTP_LENGTH
    }
}
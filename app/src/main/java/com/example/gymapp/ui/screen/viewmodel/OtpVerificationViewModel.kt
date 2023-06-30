package com.example.gymapp.ui.screen.viewmodel

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.gymapp.ui.screen.viewmodel.enum.OtpVerificationCode
import com.example.gymapp.ui.screen.viewmodel.state.OtpVerificationState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

private const val TAG = "Otp View tag"

class OtpVerificationViewModel: ViewModel() {

    private val _otpVerificationUiState = MutableStateFlow(OtpVerificationState())
    val otpVerificationUiState = _otpVerificationUiState.asStateFlow()

    fun verifyOtp(otp: String): OtpVerificationCode {
        //TODO: call backend server to verify otp
        Log.d(TAG, "inside verifyOtp function, otp = ${otp}")
        return OtpVerificationCode.SUCCEEDED
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

    companion object {
        val factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                OtpVerificationViewModel()
            }
        }
    }
}
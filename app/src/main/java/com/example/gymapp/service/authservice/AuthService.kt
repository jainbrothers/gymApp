package com.example.gymapp.service.authservice

import com.example.gymapp.ui.screen.enumeration.ErrorCode
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.flow.MutableStateFlow

interface AuthService {
    val otpVerificationState: MutableStateFlow<OtpVerificationState>
    fun authenticate(phone: String): ErrorCode
    fun onCodeSent(
        verificationId: String,
        token:
        PhoneAuthProvider.ForceResendingToken
    )

    fun verifyOtp(code: String)

    fun onVerificationCompleted(
        credential: PhoneAuthCredential
    )

    fun onVerificationFailed(exception: Exception)
    fun getUserPhone(): String
}
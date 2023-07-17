package com.example.gymapp.service.authservice

import android.util.Log
import com.example.gymapp.MainActivity
import com.example.gymapp.R
import com.example.gymapp.ui.screen.enumeration.ErrorCode
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private val TAG = AuthService::class.java.simpleName
class AuthServiceImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val context: MainActivity
    ): AuthService
{
    //TODO: Make it flow instead of stateflow
    override val otpVerificationStatus: MutableStateFlow<OtpVerificationStatus> =
        MutableStateFlow(OtpVerificationStatus())
    var savedVerificationId: String = ""
    var saveToken: PhoneAuthProvider.ForceResendingToken ?= null
    private val authCallbacks = object :
        PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            Log.d(TAG, "onVerificationCompleted called. credential=$credential")
            signInWithAuthCrendetial(credential)
        }
        override fun onVerificationFailed(exception: FirebaseException) {
            Log.d(TAG, "onVerificationFailed called. exception=$exception")
            otpVerificationStatus.update { currentOtpVerificationStatus ->
                currentOtpVerificationStatus.copy(
                    errorCode = ErrorCode.ThirdPartyServiceException(context.getString(R.string.otp_verification_failed), exception),
                    otpVerificationState = OtpVerificationState.Failed(context.getString(R.string.verification_failed_due_to_invalid_request))
                )
            }
        }
        override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
            Log.d(TAG, "entered onCodeSent, verificationId=$verificationId token=$token")
            super.onCodeSent(verificationId, token)
            savedVerificationId = verificationId
            saveToken = token
            otpVerificationStatus.update { currentOtpVerificationStatus ->
                currentOtpVerificationStatus.copy(
                    otpVerificationState = OtpVerificationState.OtpSent("OTP code sent"),
                    errorCode = ErrorCode.None
                )
            }
        }
    }
    private val authBuilder: PhoneAuthOptions.Builder = PhoneAuthOptions.newBuilder(auth)
        .setCallbacks(authCallbacks)
        .setActivity(context)
        .setTimeout(120L, TimeUnit.SECONDS)
    private fun signInWithAuthCrendetial(credential: PhoneAuthCredential) {
        var otpVerificationState: OtpVerificationState
        var errorCode: ErrorCode
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d(TAG, "signInWithAuthCrendetial. Verification successful")
                otpVerificationState = OtpVerificationState.Successful(context.getString(R.string.mobile_otp_verification_successful))
                errorCode = ErrorCode.None
            } else {
                Log.d(TAG, "signInWithAuthCrendetial. Verification failed exception=${task.exception}")
               if (task.exception is FirebaseAuthInvalidCredentialsException) {
                   errorCode = ErrorCode.ThirdPartyServiceException(
                       context.getString(R.string.otp_verification_failed_due_to_invalid_code),
                       Exception(task.exception))
                   otpVerificationState = OtpVerificationState.Error(exception = Exception(context.getString(R.string.invalid_code)))
               } else {
                   errorCode = ErrorCode.ThirdPartyServiceException(
                       context.getString(R.string.otp_verification_failed_due_to_invalid_code),
                       Exception(task.exception))
                   otpVerificationState = OtpVerificationState.Error(task.exception)
               }
            }
            otpVerificationStatus.update { currentOtpVerificationStatus ->
                currentOtpVerificationStatus.copy(
                    errorCode = errorCode,
                    otpVerificationState = otpVerificationState
                )
            }
        }
    }
    override fun authenticate(phoneNumber: String) {
        var otpVerificationState = otpVerificationStatus.value.otpVerificationState
        var errorCode = otpVerificationStatus.value.errorCode
        try {
            Log.d(TAG, "OTP verification debugging, inside authenticate function mobile${phoneNumber}")
            val options = authBuilder.setPhoneNumber(phoneNumber).build()
            PhoneAuthProvider.verifyPhoneNumber(options)
            otpVerificationState = OtpVerificationState.OtpGenerationInProgress(context.getString(R.string.otp_dispatch_is_triggered))
            errorCode = ErrorCode.None
        } catch(exception: Exception) {
            Log.e(TAG, "Error occurred during OTP generation, ${exception.message}", exception)
            errorCode = handleException(exception, context.getString(R.string.error_occurred_during_otp_generation))
            otpVerificationState = OtpVerificationState.Failed(context.getString(R.string.trigger_dispatch_otp_failed))
        }
        otpVerificationStatus.update { currentOtpVerificationStatus ->
            currentOtpVerificationStatus.copy(
                errorCode = errorCode,
                otpVerificationState = otpVerificationState
            )
        }
    }
    private fun handleException(exception: Exception, messagePrefix: String): ErrorCode {
        val error = if (exception is FirebaseException) {
            ErrorCode.ThirdPartyServiceException(
                "$messagePrefix. Error: ${exception.message}",
                exception
            )
        } else {
            ErrorCode.InternalClientException(
                "$messagePrefix. Error: ${exception.message}",
                exception
            )
        }
        return error
    }
    override fun onVerificationCompleted(credential: PhoneAuthCredential) {
        Log.d(TAG, "OTP verification debugging, inside onVerificationCompleted function credential ${credential}")
        authCallbacks.onVerificationCompleted(credential)
    }
    override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
        Log.d(TAG, "OTP verification debugging, inside onCodeSent function verificationId ${verificationId} token ${token}")
        authCallbacks.onCodeSent(verificationId, token)
    }
    override fun onVerificationFailed(exception: Exception) {
        Log.d(TAG, "OTP verification debugging, inside onVerificationFailed function exception ${exception}")
        authCallbacks.onVerificationFailed(exception as FirebaseException)
    }
    override fun verifyOtp(code: String) {
        try {
            Log.d(TAG, "onVerifyOtp entered. Code=$code, savedVerificationId=${savedVerificationId}")
            val credential = PhoneAuthProvider.getCredential(savedVerificationId, code)
            signInWithAuthCrendetial(credential)
        } catch(exception: Exception) {
            Log.e(TAG, "Error occurred during verifyOtp, ${exception.message}", exception)
            val errorCode = handleException(exception, context.getString(R.string.error_occurred_during_otp_verification))
            otpVerificationStatus.update { currentOtpVerificationStatus ->
                currentOtpVerificationStatus.copy(
                    errorCode = errorCode
                )
            }
        }
    }
    override fun getUserPhone(): String {
        TODO("Not yet implemented")
    }
}
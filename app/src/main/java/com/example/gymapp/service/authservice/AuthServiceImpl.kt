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
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private val TAG = AuthService::class.java.simpleName
class AuthServiceImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val context: MainActivity
    ): AuthService
{
    //TODO: Make it flow instead of stateflow
    override var otpVerificationState: MutableStateFlow<OtpVerificationState> = MutableStateFlow(OtpVerificationState.NotInitialised)
        private set
    var savedVerificationId: String = ""
    var saveToken: PhoneAuthProvider.ForceResendingToken ?= null
    private val authCallbacks = object :
        PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            Log.d(TAG, "PhoneAuthProvider.OnVerificationStateChangedCallbacks called. credential=$credential")
            otpVerificationState.value = OtpVerificationState.Loading(context.getString(R.string.verification_completed))
            signInWithAuthCrendetial(credential)
        }

        override fun onVerificationFailed(exception: FirebaseException) {
            Log.d(TAG, "PhoneAuthProvider.OnVerificationStateChangedCallbacks called. exception=$exception")
        }

        override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
            super.onCodeSent(verificationId, token)
            Log.d(TAG, "entered onCodeSent, verificationId=$verificationId token=$token")
            savedVerificationId = verificationId
            saveToken = token
        }
    }

    private val authBuilder: PhoneAuthOptions.Builder = PhoneAuthOptions.newBuilder(auth)
        .setCallbacks(authCallbacks)
        .setActivity(context)
        .setTimeout(120L, TimeUnit.SECONDS)

    private fun signInWithAuthCrendetial(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d(TAG, "signInWithAuthCrendetial. Verification successful")
                otpVerificationState.value = OtpVerificationState.Successful(context.getString(R.string.mobile_otp_verification_successful))
            } else {
                Log.d(TAG, "signInWithAuthCrendetial. Verification failed exception=${task.exception}")
               if (task.exception is FirebaseAuthInvalidCredentialsException) {
                   otpVerificationState.value = OtpVerificationState.Error(exception = Exception(context.getString(R.string.invalid_code)))
               } else {
                   otpVerificationState.value = OtpVerificationState.Error(task.exception)
               }
            }
        }
    }

    override fun authenticate(phoneNumber: String): ErrorCode {
        val error = try {
            otpVerificationState.value =
                OtpVerificationState.Loading("$(context.getString(R.string.code_will_be_sent_to)) $phoneNumber")
            val options = authBuilder.setPhoneNumber(phoneNumber).build()
        PhoneAuthProvider.verifyPhoneNumber(options)
            ErrorCode.None
        } catch(exception: Exception) {
            Log.e(TAG, "Error during OTP generation, ${exception.message}", exception)
            if (exception is FirebaseException) {
                ErrorCode.ThirdPartyServiceException(
                    "Error during OTP generation. Error: ${exception.message}",
                    exception
                )
            } else {
                ErrorCode.InternalClientException(
                    "Error during OTP generation. Error: ${exception.message}",
                    exception
                )
            }
        }
        return error
    }

    override fun onVerificationCompleted(credential: PhoneAuthCredential) {
        authCallbacks.onVerificationCompleted(credential)
    }

    override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
        authCallbacks.onCodeSent(verificationId, token)
    }

    override fun onVerificationFailed(exception: Exception) {
        authCallbacks.onVerificationFailed(exception as FirebaseException)
    }

    override fun verifyOtp(code: String) {
        Log.d(TAG, "onVerifyOtp entered. Code=$code")
        val credential = PhoneAuthProvider.getCredential(savedVerificationId, code)
        signInWithAuthCrendetial(credential)
    }

    override fun getUserPhone(): String {
        TODO("Not yet implemented")
    }
}
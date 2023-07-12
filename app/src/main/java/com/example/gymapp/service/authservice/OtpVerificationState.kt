package com.example.gymapp.service.authservice

sealed class OtpVerificationState {
    object NotInitialised: OtpVerificationState()
    class Successful(val message: String?): OtpVerificationState()
    class Failed(val message: String?): OtpVerificationState()
    class Loading(val message: String?): OtpVerificationState()
    class Error(val exception: Throwable?): OtpVerificationState()
    class OtpSent(val message: String?): OtpVerificationState()
    class OtpAutoFeedDone(val message: String?): OtpVerificationState()
}
package com.example.gymapp.ui.screen.enumeration

import androidx.annotation.StringRes
import com.example.gymapp.R

enum class ScreenName(@StringRes val title: Int) {
    SPLASH_SCREEN(title = R.string.app_name),
    USER_REGISTER_SCREEN(title = R.string.onboarding),
    OTP_VERIFICATION_SCREEN( title = R.string.otp_verification),
    HOME_SCREEN(title = R.string.gym_listing),
    LOCATION_PERMISSION_SCREEN(R.string.location_permission),
    GYM_DETAILS(title = R.string.gym_details)

}
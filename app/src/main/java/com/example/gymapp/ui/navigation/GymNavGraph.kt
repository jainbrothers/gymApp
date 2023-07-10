/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.flightsearchapp.ui.navigation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gymapp.ui.screen.enumeration.ScreenName
import com.example.gymapp.ui.screen.HomeScreen
import com.example.gymapp.ui.screen.OtpVerificationScreen
import com.example.gymapp.ui.screen.SplashScreen
import com.example.gymapp.ui.screen.UserRegisterScreen

/**
 * Provides Navigation graph for the application.
 */
@Composable
fun GymNavHost(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier,
) {

    var startDestination = ScreenName.SPLASH_SCREEN.name
    NavHost(
        navController,
        startDestination,
        modifier.fillMaxWidth()
    ) {
        composable(route = ScreenName.SPLASH_SCREEN.name) {
            SplashScreen(
                navigateUnregisterUser = {
                    navController.navigate(route = ScreenName.USER_REGISTER_SCREEN.name)
                },
                navigateRegisterUser = {
                    navController.navigate(route = ScreenName.HOME_SCREEN.name)
                }
            )
        }
        composable(route = ScreenName.USER_REGISTER_SCREEN.name) {
            UserRegisterScreen(
                onSuccessfulOtpGeneration = {
                    navController.navigate(route = ScreenName.OTP_VERIFICATION_SCREEN.name)
                },
                onSkipToHomePageButtonClick = {
                    navController.navigate(route = ScreenName.HOME_SCREEN.name)
                }
            )
        }
        composable(route = ScreenName.OTP_VERIFICATION_SCREEN.name) {
            OtpVerificationScreen(
                onSuccessfulOtpVerification = {
                    navController.navigate(route = ScreenName.HOME_SCREEN.name)
                }
            )
        }
        composable(route = ScreenName.HOME_SCREEN.name) {
            HomeScreen()
        }
    }
}

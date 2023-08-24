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

package com.example.gymapp.ui.navigation

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.gymapp.R
import com.example.gymapp.ui.screen.BookSession
import com.example.gymapp.ui.screen.HomeScreen
import com.example.gymapp.ui.screen.LocationPermissionScreen
import com.example.gymapp.ui.screen.OtpVerificationScreen
import com.example.gymapp.ui.screen.ShowGymDetails
import com.example.gymapp.ui.screen.SplashScreen
import com.example.gymapp.ui.screen.UserRegisterScreen
import com.example.gymapp.ui.screen.enumeration.ScreenName

private const val TAG = "GymNavGraph.kt"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GymAppBar(
    currentScreen: ScreenName,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(stringResource(currentScreen.title)) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        }
    )
}

/**
 * Provides Navigation graph for the application.
 */
@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun GymNavHost(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier,
) {
    val defaultStartScreen = ScreenName.SPLASH_SCREEN
    // Get current back stack entry
    val backStackEntry by navController.currentBackStackEntryAsState()

    // Get the name of the current screen
    val currentScreen = ScreenName.valueOf(
        backStackEntry?.destination?.route?.split('/')?.get(0) ?: defaultStartScreen.name
    )
    Log.d(TAG, "navController.previousBackStackEntry ${navController.previousBackStackEntry}")
    Log.d(TAG, "backStackEntry?.destination?.route ${backStackEntry?.destination?.route}")
    Scaffold(
        topBar = {
            GymAppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = {navController.navigateUp()})
        }
    ) { innerPadding ->
        NavHost(
            navController,
            defaultStartScreen.name,
            modifier
                .padding(innerPadding)
                .fillMaxWidth()
        ) {
            composable(route = ScreenName.SPLASH_SCREEN.name) {
                SplashScreen(
                    unregisteredUserHandler = {
                        navController.popBackStack()
                        navController.navigate(route = ScreenName.USER_REGISTER_SCREEN.name)
                    },
                    registeredUserHandler = {
                        navController.popBackStack()
                        navController.navigate(route = ScreenName.HOME_SCREEN.name)
                    },
                    otpVerifiedUserHandler = {
                        navController.popBackStack()
                        navController.navigate(route = ScreenName.OTP_VERIFICATION_SCREEN.name)
                    }
                )
            }
            composable(route = ScreenName.USER_REGISTER_SCREEN.name) {
                UserRegisterScreen(
                    onSuccessfulOtpGeneration = {
                        navController.popBackStack()
                        navController.navigate(route = ScreenName.OTP_VERIFICATION_SCREEN.name)
                    },
                    postSuccessfulRegistration = {
                        navController.popBackStack()
                        navController.navigate(route = ScreenName.HOME_SCREEN.name)
                    }
                )
            }
            composable(route = ScreenName.OTP_VERIFICATION_SCREEN.name) {
                OtpVerificationScreen(
                    onSuccessfulOtpVerification = {
                        navController.popBackStack()
                        navController.navigate(route = ScreenName.HOME_SCREEN.name)
                    },
                    onClickChangeMobileNumberButton = {
                        navController.popBackStack()
                        navController.navigate(route = ScreenName.USER_REGISTER_SCREEN.name)
                    }
                )
            }
            composable(route = ScreenName.HOME_SCREEN.name) {
                HomeScreen(
                    onClickGymDetails = { gymId ->
                        navController.navigate(route = "${ScreenName.GYM_DETAILS.name}/${gymId}")
                    }
                )
            }
            composable(
                route = ScreenName.GYM_DETAILS.name + "/{${GYM_ID_ARGUMENT_NAME}}",
                arguments = listOf(navArgument(GYM_ID_ARGUMENT_NAME) {
                    type = NavType.StringType
                }
                )
            ) {
                ShowGymDetails()
            }
            composable(route = ScreenName.LOCATION_PERMISSION_SCREEN.name) {
                LocationPermissionScreen()
            }
            composable(
                route = ScreenName.BOOK_SESSION.name + "/{${GYM_ID_ARGUMENT_NAME}}",
                arguments = listOf(navArgument(GYM_ID_ARGUMENT_NAME) {
                        type = NavType.StringType
                    }
                )
            ) {
                BookSession()
            }
        }
    }
}

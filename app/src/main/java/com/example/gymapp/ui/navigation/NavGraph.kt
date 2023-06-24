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

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.gymapp.ui.screens.choose.FacilitySearchApp
import com.example.gymapp.ui.screens.choose.HomeDestination

/**
 * Provides Navigation graph for the application.
 */
@Composable
fun GymNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = HomeDestination.route,
        modifier = modifier
    ) {
        composable(route = HomeDestination.route) {
            FacilitySearchApp(
//                navigateToFlightSearch = {
//                    navController.navigate("${FlightDetailsNav.route}/${it}")
//                }
            )
        }

//        composable(route = FlightDetailsNav.routeWithArgs,
//            arguments = listOf(navArgument(FlightDetailsNav.itemIdArg) {
//                type = NavType.IntType
//            })
//        ) {
//            ShowFlightApp(
//                navigateBack = { navController.popBackStack() },
//                onNavigateUp = { navController.navigateUp() }
//            )
//        }
    }
}

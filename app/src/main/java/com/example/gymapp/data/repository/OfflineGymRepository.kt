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

package com.example.gymapp.data.repository

import com.example.gymapp.data.repository.GymRepository
import com.example.gymapp.model.Gym
import com.example.gymapp.network.GymApiService

class OfflineGymRepository(private val gymApiService: GymApiService) : GymRepository {
    override suspend fun getAmphibians(): List<Gym> = gymApiService.getAmphibians()
}

/**
 * Repository retrieves amphibian data from underlying data source.
 */
//interface AmphibiansRepository {
//    /** Retrieves list of amphibians from underlying data source */
//    suspend fun getAmphibians(): List<Facility>
//}

/**
 * Network Implementation of repository that retrieves amphibian data from underlying data source.
 */
//class DefaultAmphibiansRepository(
//    private val amphibiansApiService: AmphibiansApiService
//) : FacilityRepository {
//    override fun getAllItemsStream(): Flow<List<Facility>> =
//
//    /** Retrieves list of amphibians from underlying data source */
//    override suspend fun getAmphibians(): List<Facility> = amphibiansApiService.getAmphibians()
//}

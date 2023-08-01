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

import com.example.gymapp.model.Gym
import com.example.gymapp.network.GymApiService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OfflineGymRepository @Inject constructor(private val gymApiService: GymApiService) : GymRepository {
    override suspend fun getAmphibians(): List<Gym> = gymApiService.getAmphibians()
    override fun getGymDetailsWithId(id: Int): Flow<Gym> = gymApiService.getGymDetailsWithIdService(id)

//    return Gym(
//    1,
//    "TAURUS FITNESS",
//    "Gym",
//    "Rajajinagar, No 46, 2nd Floor 10th CrossWest off Chord Road Above Tata motors, Rajajinagar",
//    "This is the Gym Description",
//    imgsrc = "https://developer.android.com/codelabs/basic-android-kotlin-compose-amphibians-app/img/great-basin-spadefoot.png"
//    )

}


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
import kotlinx.coroutines.flow.Flow

/**
 * Repository that provides insert, update, delete, and retrieve of [Item] from a given data source.
 */
interface GymRepository {
    suspend fun getGyms(onGetGymsCallback : (List<Gym>) -> Unit): List<Gym>
//    suspend fun getGymsUsingFlow(onGetGymsCallback : (List<Gym>) -> Unit): List<Gym>


    fun getGymDetailsWithId(gymId: String): Flow<Gym>

    /**
     * Retrieve an item from the given data source that matches with the [id].
     */
//    fun getItemStream(id: Int): Flow<Facility?>
//
//    /**
//     * Insert item in the data source
//     */
//    suspend fun insertItem(item: Facility)
//
//    /**
//     * Delete item from the data source
//     */
//    suspend fun deleteItem(item: Facility)
//
//    /**
//     * Update item in the data source
//     */
//    suspend fun updateItem(item: Facility)

//    suspend fun getGymDetails(): Gym

}

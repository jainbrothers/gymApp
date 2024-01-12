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

package com.example.gymapp.data.repository.gym

import com.example.gymapp.data.repository.GYM_TABLE_NAME
import com.example.gymapp.model.Gym
import com.example.gymapp.ui.screen.enumeration.ErrorCode
import com.google.firebase.FirebaseException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.dataObjects
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FirebaseGymRepository @Inject constructor(private val database: FirebaseFirestore) :
    GymRepository {
    override val gyms: Flow<List<Gym>>
        get() = database.collection(GYM_TABLE_NAME).dataObjects()

    override suspend fun getGymById(docId: String): Flow<Gym?> =
        try {
            database.collection(GYM_TABLE_NAME).document(docId).dataObjects()
        } catch (firebaseException: FirebaseException) {
            throw ErrorCode.ThirdPartyServiceException("Firebase error occurred during retrival of document Id ${docId}", firebaseException)
        } catch (exception: Exception) {
            throw ErrorCode.InternalClientException("Error occurred during retrival of document Id ${docId}", exception)
        }
}
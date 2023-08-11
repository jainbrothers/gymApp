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

import android.util.Log
import com.example.gymapp.model.Address
import com.example.gymapp.model.Gym
import com.example.gymapp.model.Location
import com.example.gymapp.model.Timings
import com.example.gymapp.network.GymApiService
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import kotlin.reflect.typeOf


class FirebaseGymRepository @Inject constructor(private val database: FirebaseFirestore) :
    GymRepository {
    override suspend fun getGyms(): List<Gym> {
        val gymLst : MutableList<Gym> = mutableListOf()
        val data = database.collection("Gym")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d("sarkar", "${document.id} => ${document.data}")
                    val gym = document.toObject<Gym>()
                    gymLst.add(gym)
                }
                println("sarkar")
            }
            .addOnFailureListener { exception ->
                Log.w("sarkar", "Error getting documents.", exception)
            }
        Log.d("sarkar", "now $gymLst")
        return gymLst
    }

    override fun getGymDetailsWithId(id: Int): Flow<Gym> = flow {
        emit(
            Gym()
        )
    }
}


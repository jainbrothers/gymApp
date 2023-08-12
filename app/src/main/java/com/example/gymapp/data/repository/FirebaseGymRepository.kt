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
import com.example.gymapp.model.Gym
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class FirebaseGymRepository @Inject constructor(private val database: FirebaseFirestore) :
    GymRepository {
    override suspend fun getGyms(onGetGymsCallback : (List<Gym>) -> Unit): List<Gym> {
        val gymLst : MutableList<Gym> = mutableListOf()
        database.collection("Gym")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val gym = document.toObject<Gym>()
                    gymLst.add(gym)
                }
                // return the gymLst inside the success listener block
                onGetGymsCallback(gymLst)
            }
            .addOnFailureListener { exception ->
                Log.w("sarkar", "Error getting documents.", exception)
            }
        return gymLst
    }

//    fun getGymsUsingFlow()

//    override fun getGymDetailsWithId(id: String): Flow<Gym> {
//        val gymLst : MutableList<Gym> = mutableListOf()
//        database.collection("Gym").whereEqualTo("gym_id", id)
//            .get()
//            .addOnSuccessListener { result ->
//                for (document in result) {
//                    Log.d("sarkar", "${document.id} => ${document.data}")
//                    val gym = document.toObject<Gym>()
//                    gymLst.add(gym)
//                }
//                Log.d("sarkar", "executed the code: $gymLst")
//                // return the gymLst inside the success listener block
//                //  onGetGymsCallback(gymLst)
//            }
//            .addOnFailureListener { exception ->
//                Log.w("sarkar", "Error getting documents.", exception)
//            }
//    }


    override fun getGymDetailsWithId(id: String): Flow<Gym> = flow {
        emit(
            Gym()
        )
    }
}
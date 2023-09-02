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

import com.algolia.search.client.ClientSearch
import com.algolia.search.client.Index
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.example.gymapp.data.repository.GYM_TABLE_NAME
import com.example.gymapp.model.Gym
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.dataObjects
import kotlinx.coroutines.flow.Flow
import com.algolia.search.model.IndexName
import com.algolia.search.model.ObjectID
import com.algolia.search.model.indexing.Indexable
import com.algolia.search.model.search.Query
import com.algolia.search.helper.deserialize
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class FirebaseGymRepository @Inject constructor(private val database: FirebaseFirestore,
                                                private val index: Index
) :
    GymRepository {
    override val gyms: Flow<List<Gym>>
        get() = database.collection(GYM_TABLE_NAME).dataObjects()

    override suspend fun getGymById(docId: String): Flow<Gym?> =
        database.collection(GYM_TABLE_NAME).document(docId).dataObjects()

    override suspend fun getGymListBySearch(search: String): Flow<List<Gym>> {
        // Connect and authenticate with your Algolia app

        val response = index.run {
            search(Query(search))
        }

        val results: List<Gym> = response.hits.deserialize(Gym.serializer())

//        return gyms
        return flow {
            emit(results.toList())
        }

    // Create a new index and add a record (using the `Record` class)
//        val index = client.initIndex(indexName = IndexName("test_index"))
//        val record = Record("test_record", ObjectID("1"))
//
//        index.run {
//            saveObject(Record.serializer(), record).wait()
//        }
//
//        // Search the index and print the results
//        val response = index.run {
//            search(Query("test_record"))
//        }
//        val results: List<Record> = response.hits.deserialize(Record.serializer())
//        println(results[0])
    }
}
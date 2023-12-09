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

package com.example.gymapp.data.repository.search

import com.algolia.search.client.Index
import kotlinx.coroutines.flow.Flow
import com.algolia.search.model.search.Query
import com.algolia.search.helper.deserialize
import com.algolia.search.model.ObjectID
import com.algolia.search.model.indexing.Indexable
import com.example.gymapp.data.repository.IMAGE_URLS_FIELD_NAME
import com.example.gymapp.model.Address
import com.example.gymapp.model.GymFullTextSearch
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import javax.inject.Inject

class FullTextSearchProvider @Inject constructor(private val index: Index) :
    SearchRepository {

    override suspend fun getGymListBySearchText(searchStr: String): Flow<List<GymFullTextSearch>> {
        // Connect and authenticate with your Algolia app
        val response = index.run {
            search(Query(searchStr))
        }
        val resultsSerialised: List<Indexable> = response.hits.deserialize(GymFullTextSearch.serializer())
        val gymList: List<GymFullTextSearch> = resultsSerialised as List<GymFullTextSearch>
        return flow {
            emit(gymList)
        }
    }
}
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
import com.example.gymapp.model.GymFullTextSearch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AlgoliaFullTextSearchProvider @Inject constructor(private val index: Index) :
    FullTextSearchProvider {

    override suspend fun getGymListBySearchText(searchText: String): Flow<List<GymFullTextSearch>> {
        // Connect and authenticate with your Algolia app
        val response = index.run {
            search(Query(searchText))
        }
        val resultsSerialised: List<GymFullTextSearch> = response.hits.deserialize(GymFullTextSearch.serializer())

        return flow {
            emit(resultsSerialised)
        }
    }
}
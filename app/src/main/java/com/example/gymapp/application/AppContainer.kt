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

package com.example.gymapp.application

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.gymapp.data.FacilityDatabase
import com.example.gymapp.data.FacilityRepository
import com.example.gymapp.data.OfflineFacilityRepository
import com.example.gymapp.data.repository.UserDetailPreferencesRepository
import com.example.gymapp.data.repository.UserDetailRepository

val Context.userDetailDatastore: DataStore<Preferences> by preferencesDataStore(
    name = USER_DETAIL_PREFERENCES_NAME
)

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val facilityRepository: FacilityRepository
    val userDetailRepository: UserDetailRepository
}

/**
 * [AppContainer] implementation that provides instance of [OfflineItemsRepository]
 */
class AppDataContainer(private val context: Context) : AppContainer {
    /**
     * Implementation for [FacilityRepository]
     */
    override val facilityRepository: FacilityRepository by lazy {
        OfflineFacilityRepository(FacilityDatabase.getDatabase(context).itemDao())
    }

    override val userDetailRepository: UserDetailRepository by lazy {
        UserDetailPreferencesRepository(context.userDetailDatastore)
    }
}
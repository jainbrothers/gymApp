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
import com.example.gymapp.data.repository.GymRepository
import com.example.gymapp.data.repository.OfflineGymRepository
import com.example.gymapp.data.repository.UserDetailPreferencesRepository
import com.example.gymapp.data.repository.UserDetailRepository
import com.example.gymapp.network.GymApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import java.lang.annotation.Documented
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import javax.inject.Inject
import javax.inject.Qualifier
import javax.inject.Singleton

val Context.userDetailDatastore: DataStore<Preferences> by preferencesDataStore(
    name = USER_DETAIL_PREFERENCES_NAME
)

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val gymRepository: GymRepository
}

/**
 * [AppContainer] implementation that provides instance of [OfflineItemsRepository]
 */
class AppDataContainer @Inject constructor(@ApplicationContext private val context: Context) : AppContainer {
    private val BASE_URL = "https://android-kotlin-fun-mars-server.appspot.com/"

    /**
     * Use the Retrofit builder to build a retrofit object using a kotlinx.serialization converter
     */
    @kotlinx.serialization.ExperimentalSerializationApi
    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(BASE_URL)
        .build()

    /**
     * Retrofit service object for creating api calls
     */
    private val retrofitService: GymApiService by lazy {
        retrofit.create(GymApiService::class.java)
    }

    /**
     * Implementation for [GymRepository]
     */
    override val gymRepository: GymRepository by lazy {
        OfflineGymRepository(retrofitService)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    @UserDetailPreferenceDatastoreQualifier
    fun provideUserDetailPreferencesDatastore(@ApplicationContext context: Context): DataStore<Preferences>
    {
        return context.userDetailDatastore
    }
}

@InstallIn(SingletonComponent::class)
@Module
abstract class UserDetailRepositoryModule {
    @Binds
    abstract fun bindUserDetailRepository(
        userDetailPreferencesRepository: UserDetailPreferencesRepository,
    ): UserDetailRepository
}

@InstallIn(SingletonComponent::class)
@Module
abstract class AppContainerModule {
    @Binds
    abstract fun bindAppContainer(appDataContainer: AppDataContainer): AppContainer
}

@Qualifier
@Documented
@Retention(RetentionPolicy.RUNTIME)
annotation class UserDetailPreferenceDatastoreQualifier()
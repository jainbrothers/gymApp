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

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.gymapp.MainActivity
import com.example.gymapp.data.repository.GymRepository
import com.example.gymapp.data.repository.OfflineGymRepository
import com.example.gymapp.data.repository.UserDetailPreferencesRepository
import com.example.gymapp.data.repository.UserDetailRepository
import com.example.gymapp.domain.location.DefaultLocationTracker
import com.example.gymapp.domain.location.LocationTracker
import com.example.gymapp.network.GymApiService
import com.example.gymapp.service.authservice.AuthService
import com.example.gymapp.service.authservice.AuthServiceImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
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

    @Singleton
    @Provides
    fun provideRetrofitService(): GymApiService
    {
        return Retrofit.Builder()
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .baseUrl("https://android-kotlin-fun-mars-server.appspot.com/")
            .build().create(GymApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth = Firebase.auth
    @Singleton
    @Provides
    fun provideMainActivity(): MainActivity = MainActivity.getInstance() as MainActivity

    @Provides
    @Singleton
    fun providesFusedLocationProviderClient(
        application: Application
    ): FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(application)

    @Provides
    @Singleton
    fun providesLocationTracker(
        fusedLocationProviderClient: FusedLocationProviderClient,
        application: Application
    ): LocationTracker = DefaultLocationTracker(
        fusedLocationProviderClient = fusedLocationProviderClient,
        application = application
    )
}

@InstallIn(SingletonComponent::class)
@Module
abstract class GymRepositoryModule {
    @Binds
    abstract fun bindGymRepository(
        offlineGymRepository: OfflineGymRepository
    ): GymRepository
}

@InstallIn(SingletonComponent::class)
@Module
abstract class UserDetailRepositoryModule {
    @Binds
    abstract fun bindUserDetailRepository(
        userDetailPreferencesRepository: UserDetailPreferencesRepository,
    ): UserDetailRepository
}
@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceModule {
    @Binds
    abstract fun bindAccountService(
        authServiceImpl: AuthServiceImpl
    ): AuthService
}
@Qualifier
@Documented
@Retention(RetentionPolicy.RUNTIME)
annotation class UserDetailPreferenceDatastoreQualifier()
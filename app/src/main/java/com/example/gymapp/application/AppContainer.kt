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
import androidx.lifecycle.ViewModel
import com.example.gymapp.MainActivity
import com.example.gymapp.data.repository.user.FirebaseUserRepository
import com.example.gymapp.data.repository.gym.GymRepository
import com.example.gymapp.data.repository.gym.FirebaseGymRepository
import com.example.gymapp.data.repository.UserDetailPreferencesRepository
import com.example.gymapp.data.repository.UserDetailRepository
import com.example.gymapp.data.repository.user.UserRepository
import com.example.gymapp.network.GymApiService
import com.example.gymapp.service.authservice.AuthService
import com.example.gymapp.service.authservice.AuthServiceImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import java.lang.annotation.Documented
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
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
    fun provideFirebaseDB(): FirebaseFirestore = Firebase.firestore
    @Singleton
    @Provides
    fun provideMainActivity(): MainActivity = MainActivity.getInstance() as MainActivity
}

@InstallIn(SingletonComponent::class)
@Module
abstract class GymRepositoryModule {
    @Binds
    abstract fun bindGymRepository(
        firebaseGymRepository: FirebaseGymRepository
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
    @Singleton
    abstract fun bindAuthService(
        authServiceImpl: AuthServiceImpl
    ): AuthService
    @Binds
    @Singleton
    abstract fun bindUserRepository(
        firebaseUserRepository: FirebaseUserRepository
    ): UserRepository
}

@Qualifier
@Documented
@Retention(RetentionPolicy.RUNTIME)
annotation class UserDetailPreferenceDatastoreQualifier()
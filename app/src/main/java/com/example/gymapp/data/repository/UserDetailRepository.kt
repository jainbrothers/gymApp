package com.example.gymapp.data.repository

import com.example.gymapp.ui.screen.viewmodel.enumeration.UserRegistrationState
import kotlinx.coroutines.flow.Flow

interface UserDetailRepository {
    val userMobileNumber: Flow<String>
    val userRegistrationStatus: Flow<UserRegistrationState>
    val userId: Flow<String>
    suspend fun saveUserMobileNumber(mobileNumber: String)
    suspend fun saveUserRegistrationState(userRegistrationState: UserRegistrationState)
    suspend fun saveUserId(userId: String)
}
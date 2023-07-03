package com.example.gymapp.data.repository

import com.example.gymapp.ui.screen.viewmodel.enum.UserRegistrationState
import kotlinx.coroutines.flow.Flow

interface UserDetailRepository {
    val userMobileNumber: Flow<String>
    val userRegistrationStatus: Flow<UserRegistrationState>
    suspend fun saveUserMobileNumber(mobileNumber: String)
    suspend fun saveUserRegistrationState(userRegistrationState: UserRegistrationState)
}
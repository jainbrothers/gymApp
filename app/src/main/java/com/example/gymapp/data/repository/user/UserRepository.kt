package com.example.gymapp.data.repository.user

import com.example.gymapp.model.User
import com.example.gymapp.ui.screen.enumeration.ErrorCode
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun create(user: User)
    suspend fun getbyId(userId: String): Flow<User?>
    suspend fun getbyMobileNumber(mobileNumber: String, callback: (User?, ErrorCode) -> Unit)
}
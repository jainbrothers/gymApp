package com.example.gymapp.data.repository.user

import com.example.gymapp.model.User
import com.example.gymapp.ui.screen.enumeration.ErrorCode
interface UserRepository {
    suspend fun create(user: User)
    suspend fun getbyId(userId: String): User?
    suspend fun getbyMobileNumber(mobileNumber: String, callback: (User?, ErrorCode) -> Unit)
}
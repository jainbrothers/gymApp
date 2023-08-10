package com.example.gymapp.data.repository

import com.example.gymapp.model.User

interface UserRepository {
    suspend fun create(user: User)
    suspend fun getbyId(userId: String): User
    suspend fun getbyMobileNumber(mobileNumber: String): User
}
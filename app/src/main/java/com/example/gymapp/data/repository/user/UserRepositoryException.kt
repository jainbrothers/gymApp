package com.example.gymapp.data.repository.user

sealed class UserRepositoryException: RuntimeException() {
    class UserNotFound(message: String): UserRepositoryException()
}
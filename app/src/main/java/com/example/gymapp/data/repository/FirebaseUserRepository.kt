package com.example.gymapp.data.repository

import android.util.Log
import com.example.gymapp.model.User
import com.example.gymapp.network.GymApiService
import com.google.firebase.firestore.FirebaseFirestore
import java.util.UUID
import javax.inject.Inject

private const val TAG: String = "FirebaseUserRepository"

class FirebaseUserRepository @Inject constructor(private val database: FirebaseFirestore): UserRepository {
    override suspend fun create(user: User) {
        database.collection("user")
            .add(user)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error adding document", e)
                throw Exception(e)
            }
    }
    override suspend fun getbyId(userId: String): User {
        TODO("Not yet implemented")
    }
    override suspend fun getbyMobileNumber(mobileNumber: String): User {
        TODO("Not yet implemented")
    }
}
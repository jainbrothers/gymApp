package com.example.gymapp.data.repository.user

import android.util.Log
import com.example.gymapp.model.User
import com.example.gymapp.ui.screen.enumeration.ErrorCode
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import javax.inject.Inject


private const val TAG: String = "FirebaseUserRepository"

class FirebaseUserRepository @Inject constructor(private val database: FirebaseFirestore):
    UserRepository {
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
    override suspend fun getbyMobileNumber(mobileNumber: String, callback: (User?) -> Unit) {
        Log.d(TAG, "RRD entered into getbyMobileNumber mobilenumber ${mobileNumber}")
        lateinit var user: User
        database.collection("user")
            .whereEqualTo(MOBILE_NUMBER_FIELD_NAME, mobileNumber)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.size() == 0) {
                    throw UserRepositoryException.UserNotFound("Record with mobile number ${mobileNumber} not found")
                }
                user = documents.first().toObject<User>()
                Log.d(TAG, "RRD DB returned user ${user}")
                callback(user)
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error getting documents for mobile number ${mobileNumber}", exception)
                throw ErrorCode.ThirdPartyServiceException("Firestore failure while fetching user record", exception)
            }
    }
}
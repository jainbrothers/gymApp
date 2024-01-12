package com.example.gymapp.data.repository.user

import android.util.Log

import com.example.gymapp.data.repository.MOBILE_NUMBER_FIELD_NAME
import com.example.gymapp.data.repository.GYM_TABLE_NAME
import com.example.gymapp.data.repository.MOBILE_NUMBER_FIELD_NAME
import com.example.gymapp.data.repository.USER_TABLE_NAME
import com.example.gymapp.model.Gym
import com.example.gymapp.model.User
import com.example.gymapp.ui.screen.enumeration.ErrorCode
import com.example.gymapp.ui.screen.viewmodel.OtpVerificationViewModel
import com.google.firebase.FirebaseException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.dataObjects
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.reflect.KFunction2


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
    override suspend fun getbyId(docId: String): Flow<User?> {
        try {
            return database.collection(USER_TABLE_NAME).document(docId).dataObjects()
        } catch (firebaseException: FirebaseException) {
            throw ErrorCode.ThirdPartyServiceException("Firebase error occurred during retrival of document Id ${docId}", firebaseException)
        } catch (exception: Exception) {
            throw ErrorCode.InternalClientException("Error occurred during retrival of document Id ${docId}", exception)
        }
    }

    override suspend fun getbyMobileNumber(mobileNumber: String, callback: (User?, ErrorCode) -> Unit) {
        Log.d(TAG, "Entered into getbyMobileNumber mobilenumber ${mobileNumber}")
        var user: User? = null
        var errorCode: ErrorCode = ErrorCode.None
        database.collection("user")
            .whereEqualTo(MOBILE_NUMBER_FIELD_NAME, mobileNumber)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.size() > 0) {
                    user = documents.first().toObject<User>()
                }
                Log.d(TAG, "Fetch user record user = ${user}")
                callback(user, errorCode)
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error getting documents for mobile number ${mobileNumber}", exception)
                errorCode = ErrorCode.ThirdPartyServiceException("Firestore failure while fetching user record", exception)
                callback(user, errorCode)
            }
    }
}
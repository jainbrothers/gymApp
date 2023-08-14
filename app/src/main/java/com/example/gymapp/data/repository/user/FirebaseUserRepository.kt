package com.example.gymapp.data.repository.user

import android.util.Log
import com.example.gymapp.data.repository.MOBILE_NUMBER_FIELD_NAME
import com.example.gymapp.model.User
import com.example.gymapp.ui.screen.enumeration.ErrorCode
import com.example.gymapp.ui.screen.viewmodel.OtpVerificationViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
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
    override suspend fun getbyId(userId: String): User {
        TODO("Not yet implemented")
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
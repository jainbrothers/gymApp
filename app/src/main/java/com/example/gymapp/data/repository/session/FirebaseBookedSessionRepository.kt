package com.example.gymapp.data.repository.session

import android.util.Log
import com.example.gymapp.data.repository.BOOKED_SESSION_TABLE_NAME
import com.example.gymapp.model.session.BookedSessionDetail
import com.example.gymapp.ui.screen.enumeration.ErrorCode
import com.google.firebase.FirebaseException
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

private val TAG = FirebaseBookedSessionRepository::class.java.simpleName
class FirebaseBookedSessionRepository @Inject constructor(private val database: FirebaseFirestore): BookedSessionRepository {
    override suspend fun create(bookedSessionDetail: BookedSessionDetail, docId: String) {
        Log.d(TAG, "Entered create, bookedSessionDetail = ${bookedSessionDetail}, docId = ${docId}")
        try {
            database.collection(BOOKED_SESSION_TABLE_NAME).document(docId).set(bookedSessionDetail)
        } catch (firebaseException: FirebaseException) {
            throw ErrorCode.ThirdPartyServiceException("Firebase error occurred while adding booked session detail: ${bookedSessionDetail}", firebaseException)
        } catch (exception: Exception) {
            throw ErrorCode.InternalClientException("Error occurred while adding booked session detail: ${bookedSessionDetail}", exception)
        }
        Log.d(TAG, "Exiting create, docId = ${docId}")
    }
}
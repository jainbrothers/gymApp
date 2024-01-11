package com.example.gymapp.data.repository.session

import com.example.gymapp.data.repository.BOOKED_SESSION_TABLE_NAME
import com.example.gymapp.data.repository.GYM_TABLE_NAME
import com.example.gymapp.model.session.BookedSessionDetail
import com.example.gymapp.ui.screen.enumeration.ErrorCode
import com.google.firebase.FirebaseException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.dataObjects
import javax.inject.Inject

class FirebaseBookedSessionRepository @Inject constructor(private val database: FirebaseFirestore): BookedSessionRepository {
    override suspend fun create(bookedSessionDetail: BookedSessionDetail, docId: String) {
        try {
            database.collection(BOOKED_SESSION_TABLE_NAME).document(docId).set(bookedSessionDetail)
        } catch (firebaseException: FirebaseException) {
            throw ErrorCode.ThirdPartyServiceException("Firebase error occurred while adding booked session detail: ${bookedSessionDetail}", firebaseException)
        } catch (exception: Exception) {
            throw ErrorCode.InternalClientException("Error occurred while adding booked session detail: ${bookedSessionDetail}", exception)
        }
    }
}
package com.example.gymapp.data.repository.session

import com.example.gymapp.model.session.BookedSessionDetail

interface BookedSessionRepository {
    suspend fun create(bookedSessionDetail: BookedSessionDetail, docId: String): Unit
}
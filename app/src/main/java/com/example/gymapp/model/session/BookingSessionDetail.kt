package com.example.gymapp.model.session

data class BookingSessionDetail (
    val gymId: String,
    val durationInMinute: Int,
    val sessionStartEpochInMillis: Long,
    val activityId: String
)
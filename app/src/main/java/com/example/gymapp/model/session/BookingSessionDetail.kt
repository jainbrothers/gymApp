package com.example.gymapp.model.session

data class BookingSessionDetail (
    val gymId: String,
    val durationInMinute: Int,
    val sessionStartEpochInMilli: Long
)
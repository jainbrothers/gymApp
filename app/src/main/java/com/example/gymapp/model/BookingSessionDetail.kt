package com.example.gymapp.model

data class BookingSessionDetail (
    val gymId: String,
    val durationInMinute: Int,
    val sessionStartEpochInMilli: Long
)
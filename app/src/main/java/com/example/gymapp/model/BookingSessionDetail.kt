package com.example.gymapp.model

data class BookingSessionDetail (
    val sessionTime: String,
    val gymId: String,
    val dateDisplayName: String,
    val durationInMinute: Int,
    val sessionStartTimestamp: Long
)
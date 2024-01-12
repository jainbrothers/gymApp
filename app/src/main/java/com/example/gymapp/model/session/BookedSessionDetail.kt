package com.example.gymapp.model.session

import com.example.gymapp.model.payment.PaymentDetail

data class BookedSessionDetail (
    val activityId: String,
    val durationInMinute: Int,
    val gymId: String,
    val paymentDetail: PaymentDetail?,
    val sessionStartTimeEpochInMillis: Long,
    val status: SessionStatus,
    val bookingTimeEpochInMillis: Long
)
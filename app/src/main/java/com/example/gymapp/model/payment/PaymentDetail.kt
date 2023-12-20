package com.example.gymapp.model.payment

data class PaymentDetail(
    val amountDue: Float,
    val amountPaid: Float,
    val discountInValue: Float,
    val price: Float
)
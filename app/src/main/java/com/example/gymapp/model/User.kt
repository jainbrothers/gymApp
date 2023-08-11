package com.example.gymapp.model

import com.google.firebase.firestore.PropertyName
data class User(
    @get:PropertyName("user_id")
    val userId: String,
    @get:PropertyName("mobile_number")
    val mobileNumber: String
)
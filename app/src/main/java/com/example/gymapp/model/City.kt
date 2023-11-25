package com.example.gymapp.model

import com.example.gymapp.data.repository.MOBILE_NUMBER_FIELD_NAME
import com.example.gymapp.data.repository.USER_ID_FIELD_NAME
import com.example.gymapp.data.repository.MOBILE_NUMBER_FIELD_NAME
import com.example.gymapp.data.repository.USER_ID_FIELD_NAME
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName
data class City(
    @DocumentId val id: String = "",
    val city: String = ""
)
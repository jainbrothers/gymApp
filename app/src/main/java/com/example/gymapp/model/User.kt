package com.example.gymapp.model

import com.example.gymapp.data.repository.user.MOBILE_NUMBER_FIELD_NAME
import com.example.gymapp.data.repository.user.USER_ID_FIELD_NAME
import com.google.firebase.firestore.PropertyName
data class User(
    @get:PropertyName(USER_ID_FIELD_NAME)
    @set:PropertyName(USER_ID_FIELD_NAME)
    var userId: String = "",
    @get:PropertyName(MOBILE_NUMBER_FIELD_NAME)
    @set:PropertyName(MOBILE_NUMBER_FIELD_NAME)
    var mobileNumber: String = ""
//    val location: Location
)
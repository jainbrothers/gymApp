/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.gymapp.model

import com.example.gymapp.data.repository.BEGIN_HOUR_FIELD_NAME
import com.example.gymapp.data.repository.BEGIN_MINUTE_FIELD_NAME
import com.example.gymapp.data.repository.END_HOUR_FIELD_NAME
import com.example.gymapp.data.repository.END_MINUTE_FIELD_NAME
import com.example.gymapp.data.repository.IMAGE_URLS_FIELD_NAME
import com.example.gymapp.data.repository.PIN_CODE_FIELD_NAME
import com.example.gymapp.data.repository.SCHEDULE_LIST_FIELD_NAME
import com.example.gymapp.data.repository.STREET_NAME_AND_NUMBER_FIELD_NAME
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName
/**
 * Data class that defines an amphibian which includes a name, type, description, and image URL.
 */

data class Session(
    val userId: String,
    val gymId: String,
    val beginHour: Int,
    val beginMinute: Int,
    val endHour: Int,
    val endMinute: Int,
    val activity: String,
    val isCancelled: Boolean
)

data class Timings(
    val day: String = "",
    @get:PropertyName(BEGIN_HOUR_FIELD_NAME)
    @set:PropertyName(BEGIN_HOUR_FIELD_NAME)
    var openingHour: Int = 0,
    @get:PropertyName(BEGIN_MINUTE_FIELD_NAME)
    @set:PropertyName(BEGIN_MINUTE_FIELD_NAME)
    var openingMinute: Int = 0,
    @get:PropertyName(END_HOUR_FIELD_NAME)
    @set:PropertyName(END_HOUR_FIELD_NAME)
    var closingHour: Int = 0,
    @get:PropertyName(END_MINUTE_FIELD_NAME)
    @set:PropertyName(END_MINUTE_FIELD_NAME)
    var closingMinute: Int = 0
)

data class Location(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
)

data class Address(
    @get:PropertyName(STREET_NAME_AND_NUMBER_FIELD_NAME)
    @set:PropertyName(STREET_NAME_AND_NUMBER_FIELD_NAME)
    var streetNameAndNumber: String = "",
    val locality: String = "",
    val city: String = "",
    @get:PropertyName(PIN_CODE_FIELD_NAME)
    @set:PropertyName(PIN_CODE_FIELD_NAME)
    var pinCode: String = "",
    val location: Location = Location()
)

data class Gym(
    @DocumentId val id: String = "",
    val name: String = "",
    val type: String = "",
    val activities: List<String> = listOf(),
    val address: Address = Address(),
    val description: String = "",
    @get:PropertyName(IMAGE_URLS_FIELD_NAME)
    @set:PropertyName(IMAGE_URLS_FIELD_NAME)
    var imageUrls: List<String> = listOf(),
    val timings: List<Timings> = listOf(),
    val amenities: List<String> = listOf(),
    @get:PropertyName(SCHEDULE_LIST_FIELD_NAME)
    @set:PropertyName(SCHEDULE_LIST_FIELD_NAME)
    var activityToDayToScheduleListMap: Map<String, Map<String, List<SessionSchedule>>> = emptyMap()
    )

data class SessionSchedule(
    @get:PropertyName(BEGIN_HOUR_FIELD_NAME)
    @set:PropertyName(BEGIN_HOUR_FIELD_NAME)
    var beginHour: Int = 0,
    @get:PropertyName(BEGIN_MINUTE_FIELD_NAME)
    @set:PropertyName(BEGIN_MINUTE_FIELD_NAME)
    var beginMinute: Int = 0,
    @get:PropertyName(END_HOUR_FIELD_NAME)
    @set:PropertyName(END_HOUR_FIELD_NAME)
    var endHour: Int = 0,
    @get:PropertyName(END_MINUTE_FIELD_NAME)
    @set:PropertyName(END_MINUTE_FIELD_NAME)
    var endMinute: Int = 0
)
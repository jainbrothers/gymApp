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

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data class that defines an amphibian which includes a name, type, description, and image URL.
 */

@Serializable
data class Timings(
    val day: String,
    val beginHour: Int,
    val beginMinute: Int,
    val endHour: Int,
    val endMinute: Int
)

@Serializable
data class Location(
    val latitude: Double,
    val longitude: Double
)

@Serializable
data class Address(
    val id: Int,
    val streetNameAndNumber: String,
    val locality: String,
    val city: String,
    val pinCode: Int,
    val location: Location
)

//@Entity
@Serializable
data class Gym(
//    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val type: String,
    val activities: List<String> = listOf("GYM"),
    val address: Address = Address(
        1,
        "3rd & 4th floor. 12th Cross Rd",
        "Mahalakshmi Layout",
        "Bengaluru",
        560010,
        Location(
            3.2323,
            4.232323
        )
    ),
    val description: String,
    @SerialName("img_src") val imageUrls: String,
    val timings: List<Timings> =  listOf(
        Timings("Mon", 6, 0, 22, 0),
        Timings("Tue", 6, 0, 22, 0),
        Timings("Wed", 6, 0, 22, 0),
        Timings("Thu", 6, 0, 22, 0),
        Timings("Fri", 6, 0, 22, 0),
        Timings("Sat", 6, 0, 22, 0),
        Timings("Sun", 6, 0, 22, 0)
    ),
    val amenities: List<String> = listOf(
        "Parking", "CCTV", "Locker"
    )
)

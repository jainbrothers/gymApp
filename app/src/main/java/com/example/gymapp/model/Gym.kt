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
import com.google.firebase.firestore.PropertyName
/**
 * Data class that defines an amphibian which includes a name, type, description, and image URL.
 */


data class Timings(
    val day: String = "",
    @get:PropertyName("begin_hour") val beginHour: Int = 0,
    @get:PropertyName("begin_minute") val beginMinute: Int = 0,
    @get:PropertyName("end_hour") val endHour: Int = 0,
    @get:PropertyName("end_minute") val endMinute: Int = 0
)

data class Location(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
)

data class Address(
    @get:PropertyName("street_name_and_number") val streetNameAndNumber: String = "",
    val locality: String = "",
    val city: String = "",
    val pinCode: String = "",
    val location: Location = Location()
)

data class Gym(
    @PropertyName("gym_id") val id: String = "",
    val name: String = "",
    val type: String = "",
    val activities: List<String> = listOf(),
    val address: Address = Address(),
    val description: String = "",
    @PropertyName("gym_src") val imageUrls: List<String> = listOf(), // gym_src -> gym_urls
    val timings: List<Timings> = listOf(),
    val amenities: List<String> = listOf()
    )

//@Entity
//@Serializable
//data class Gym(
////    @PrimaryKey(autoGenerate = true)
//    val id: Int = 0,
//    val name: String,
//    val type: String,
//    val activities: List<String> = listOf("GYM", "Zumba", "YOGA", "HIIT"),
//    val address: Address = Address(
//        1,
//        "77, Ground Floor, Below Stories Pub",
//        "Mahalakshmi Metro Nandini Layout",
//        "Bengaluru",
//        560010,
//        Location(
//            13.00868,77.54906
//        )
//    ),
//    val description: String,
//    @SerialName("img_src") val imageUrls: String,
//    val timings: List<Timings> =  listOf(
//        Timings("Mon", 6, 0, 22, 0),
//        Timings("Tue", 6, 0, 22, 0),
//        Timings("Wed", 6, 0, 22, 0),
//        Timings("Thu", 6, 0, 22, 0),
//        Timings("Fri", 6, 0, 22, 0),
//        Timings("Sat", 6, 0, 22, 0),
//        Timings("Sun", 6, 0, 22, 0)
//    ),
//    val amenities: List<String> = listOf(
//        "Parking", "CCTV", "Locker"
//    )
//)

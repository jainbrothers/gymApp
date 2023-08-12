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
    @get:PropertyName("begin_hour")
    @set:PropertyName("begin_hour")
    var beginHour: Int = 0,
    @get:PropertyName("begin_minute")
    @set:PropertyName("begin_minute")
    var beginMinute: Int = 0,
    @get:PropertyName("end_hour")
    @set:PropertyName("end_hour")
    var endHour: Int = 0,
    @get:PropertyName("end_minute")
    @set:PropertyName("end_minute")
    var endMinute: Int = 0
)

data class Location(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
)

data class Address(
    @get:PropertyName("street_name_and_number")
    @set:PropertyName("street_name_and_number")
    var streetNameAndNumber: String = "",
    val locality: String = "",
    val city: String = "",
//    @get:PropertyName("pin_code")
//    @set:PropertyName("pin_code")
//    var pinCode: String = "",
    val location: Location = Location()
)

data class Gym(
    @get:PropertyName("gym_id")
    @set:PropertyName("gym_id")
    var id: String = "",
    val name: String = "",
    val type: String = "",
    val activities: List<String> = listOf(),
    val address: Address = Address(),
    val description: String = "",
    @get:PropertyName("gym_src")
    @set:PropertyName("gym_src")
    var imageUrls: List<String> = listOf(), // gym_src -> gym_urls
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

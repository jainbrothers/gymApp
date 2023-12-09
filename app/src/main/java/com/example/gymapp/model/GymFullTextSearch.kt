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

import com.example.gymapp.data.repository.IMAGE_URLS_FIELD_NAME
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName
import com.algolia.search.model.indexing.Indexable
import kotlinx.serialization.Serializable
import com.algolia.search.model.ObjectID
import kotlinx.serialization.SerialName

@Serializable
data class GymFullTextSearch(
    val name: String = "",
    val type: String = "",
    val activities: List<String> = listOf(),
    val address: Address = Address(),
    @get:PropertyName(IMAGE_URLS_FIELD_NAME)
    @set:PropertyName(IMAGE_URLS_FIELD_NAME)
    @SerialName(IMAGE_URLS_FIELD_NAME)
    var imageUrls: List<String> = listOf(),
    override val objectID: ObjectID = ObjectID(""),
    @DocumentId val id: String = objectID.toString()
//    @DocumentId val id: String = ""

) :Indexable

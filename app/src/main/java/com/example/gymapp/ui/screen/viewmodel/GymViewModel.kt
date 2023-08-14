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

package com.example.gymapp.ui.screen.viewmodel

import androidx.lifecycle.ViewModel
import com.example.gymapp.data.repository.gym.GymRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject



@HiltViewModel
class GymViewModel @Inject constructor(
    val gymRepository: GymRepository
//    val userRepository: UserRepository // to get from local data store - preference
) : ViewModel() {

//    userRepository
//    private val mobileNumber: String = checkNotNull(savedStateHandle["mobileNumber"])
    val gyms = gymRepository.gyms

//    init {
////        getUserDetails(mobileNumber)
//        getLocationDetails()
//    }

//    private fun getUserDetails(mobileNumber: String) {
//        viewModelScope.launch {
//            // val user = userRepository.getbyMobileNumber(mobileNumber)
//        }
//    }

//    private fun getLocationDetails() {
//        viewModelScope.launch {
//            // val user = userRepository.getbyMobileNumber(mobileNumber)
//
//        }
//    }

}


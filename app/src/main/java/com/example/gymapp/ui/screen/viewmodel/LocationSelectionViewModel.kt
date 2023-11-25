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


import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymapp.data.repository.UserDetailRepository
import com.example.gymapp.data.repository.user.UserRepository
import com.example.gymapp.model.User
import com.example.gymapp.ui.screen.viewmodel.state.GymListingUiState
import com.example.gymapp.ui.screen.viewmodel.state.HomeScreenUiState
import com.example.gymapp.ui.screen.viewmodel.state.SplashScreenUiState
import com.example.gymapp.ui.screen.viewmodel.state.UserUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LocationSelectionViewModel @Inject constructor(
    val userDetailRepository: UserDetailRepository,
    val userRepository: UserRepository
) : ViewModel() {
    val userUiState = MutableStateFlow(UserUiState())

    init {
        getUser()
    }

//    val uiState: StateFlow<UserUiState> = userRepository..map { registrationState ->
//        Log.d("TAG", "registration state ${registrationState}")
//        SplashScreenUiState(userRegistrationState = registrationState,
//            isLoadingDone = true,
//            mobileNumber=userDetailRepository.userMobileNumber.first())
//    }.stateIn(
//        viewModelScope,
//        SharingStarted.WhileSubscribed(5000L),
//        SplashScreenUiState()
//    )


//    val uiState: StateFlow<HomeScreenUiState> =
//         { userState ->
//            UserUiState(
//                user = userState.user
//            )
//        }.stateIn(
//            viewModelScope,
//            SharingStarted.WhileSubscribed(5000L),
//            (HomeScreenUiState())
//        )

    private fun getUser() {
        viewModelScope.launch {
            userRepository.getbyId(userId = userDetailRepository.userId.first()).collect { user ->
                userUiState.update { currentState ->
                    currentState.copy(
                        user = user ?: User()
                    )
                }
            }
        }
    }

}


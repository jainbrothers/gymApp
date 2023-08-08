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

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymapp.data.repository.GymRepository
import com.example.gymapp.model.Gym
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

/**
 * Ui State for HomeScreen
 */
data class HomeUiState(val gymList: List<Gym> = listOf())

sealed interface GymsUiState {
    data class Success(val gyms: List<Gym>) : GymsUiState
    object Error : GymsUiState
    object Loading : GymsUiState
}

/**
 * [BlurViewModel] starts and stops the WorkManger and applies blur to the image. Also updates the
 * visibility states of the buttons depending on the states of the WorkManger.
 */
@HiltViewModel
class GymViewModel @Inject constructor(val gymRepository: GymRepository) : ViewModel() {

    var gymsUiState: GymsUiState by mutableStateOf(GymsUiState.Loading)
        private set

    init {
        getGyms()
    }

    private fun getGyms() {
        viewModelScope.launch {
            gymsUiState = GymsUiState.Loading
            gymsUiState = try {
                GymsUiState.Success(gymRepository.getGyms())
            } catch (e: IOException) {
                GymsUiState.Error
            } catch (e: HttpException) {
                GymsUiState.Error
            }
        }
    }

}


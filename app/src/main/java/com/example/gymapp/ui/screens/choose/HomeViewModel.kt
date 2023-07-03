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

package com.example.gymapp.ui.screens.choose

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.gymapp.application.FacilityApplication
import com.example.gymapp.data.Facility
import com.example.gymapp.data.FacilityRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

/**
 * Ui State for HomeScreen
 */
data class HomeUiState(val facilityList: List<Facility> = listOf())

/**
 * [BlurViewModel] starts and stops the WorkManger and applies blur to the image. Also updates the
 * visibility states of the buttons depending on the states of the WorkManger.
 */
class HomeViewModel(private val facilityRepository: FacilityRepository) : ViewModel() {
    val homeUiState: StateFlow<HomeUiState> =
        facilityRepository.getAllItemsStream().map { HomeUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = HomeUiState()
            )

    /**
     * Factory for [HomwViewModel] that takes [BluromaticRepository] as a dependency
     */
    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
        val factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val facilityRepository =
                    facilityApplication().container.facilityRepository
                HomeViewModel(
                    facilityRepository = facilityRepository
                )
            }
        }
    }
}

fun CreationExtras.facilityApplication(): FacilityApplication =
    (this[APPLICATION_KEY] as FacilityApplication)


//    internal val blurAmount = BlurAmountData.blurAmount
//
//    val blurUiState: StateFlow<BlurUiState> = bluromaticRepository.outputWorkInfo.map { info ->
//        val outputImageUri = info.outputData.getString(KEY_IMAGE_URI)
//        when {
//            info.state.isFinished && !outputImageUri.isNullOrEmpty() -> {
//                BlurUiState.Complete(outputUri = outputImageUri)
//            }
//            info.state == WorkInfo.State.CANCELLED -> {
//                BlurUiState.Default
//            }
//            else -> BlurUiState.Loading
//        }
//    }.stateIn(
//        scope = viewModelScope,
//        started = SharingStarted.WhileSubscribed(5_000),
//        initialValue = BlurUiState.Default
//    )
//
//    //MutableStateFlow(BlurUiState.Default)
//
//    /**
//     * Call the method from repository to create the WorkRequest to apply the blur
//     * and save the resulting image
//     * @param blurLevel The amount to blur the image
//     */
//    fun applyBlur(blurLevel: Int) {
//        bluromaticRepository.applyBlur(blurLevel)
//    }
//
//    fun cancelWork() {
//        bluromaticRepository.cancelWork()
//    }
//

//sealed interface BlurUiState {
//    object Default : BlurUiState
//    object Loading : BlurUiState
//    data class Complete(val outputUri: String) : BlurUiState
//}



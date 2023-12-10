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
import com.example.gymapp.data.repository.search.AlgoliaFullTextSearchProvider
import com.example.gymapp.data.repository.search.FullTextSearchProvider
import com.example.gymapp.data.repository.user.UserRepository
import com.example.gymapp.model.User
import com.example.gymapp.ui.screen.viewmodel.state.GymListingUiState
import com.example.gymapp.ui.screen.viewmodel.state.HomeScreenUiState
import com.example.gymapp.ui.screen.viewmodel.state.UserUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class GymListingViewModel @Inject constructor(
    val userDetailRepository: UserDetailRepository,
    val userRepository: UserRepository,
    val fullTextSearchProvider: AlgoliaFullTextSearchProvider
) : ViewModel() {
    val gymListingUiState = MutableStateFlow(GymListingUiState())
    val userUiState = MutableStateFlow(UserUiState())
    var searchText by mutableStateOf("")
        private set

    init {
        getUser()
        getGymList()
    }

    val uiState: StateFlow<HomeScreenUiState> =
        gymListingUiState.combine(userUiState) { gymListState, userState ->
            HomeScreenUiState(
                user = userState.user,
                gymFullTextSearchList = gymListState.gymFullTextSearch
            )
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            (HomeScreenUiState())
        )


    private fun getGymList() {
        viewModelScope.launch {
            Log.d("getGymList", "searchQuery $searchText")
            fullTextSearchProvider.getGymListBySearchText(searchText).collect { gyms ->
                gymListingUiState.update { currentState ->
                    currentState.copy(
                        gymFullTextSearch = gyms
                    )
                }
            }
        }
    }

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

    fun updateSearchText(query: String) {
        viewModelScope.launch {
            searchText = query
            getGymList()
        }
    }
}


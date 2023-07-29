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
import javax.inject.Inject

sealed interface GymDetailsUiState {
    data class Success(val amphibians: Gym) : GymDetailsUiState
    object Error : GymDetailsUiState
    object Loading : GymDetailsUiState
}

@HiltViewModel
class GymDetailsViewModel @Inject constructor(val gymRepository: GymRepository) : ViewModel() {

    var gymDetailsUiState: GymDetailsUiState by mutableStateOf(GymDetailsUiState.Loading)
        private set

//    private fun getGymDetails(){
//        viewModelScope.launch {
//            gymDetailsUiState = try {
//
//            } catch (e:Exception){
//
//            }
//        }
//    }
}

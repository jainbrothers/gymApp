package com.example.gymapp.ui.screen.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymapp.data.repository.gym.GymRepository
import com.example.gymapp.model.Gym
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

//sealed interface GymDetailsUiState {
//    data class Successful(val amphibians: Gym) : GymDetailsUiState
//    object Error : GymDetailsUiState
//    object Loading : GymDetailsUiState
//}

data class GymDetailsUiState( // Create Gym object with None values
    val gym: Gym = Gym()
)


@HiltViewModel
class GymDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle, val gymRepository: GymRepository
) : ViewModel() {

    val gym = mutableStateOf(Gym())
    private val gymId: String = checkNotNull(savedStateHandle["gymId"])
    init{
        getGymDetails()
    }
    private fun getGymDetails() {
        viewModelScope.launch {
            gym.value = gymRepository.getGymWithId(gymId) ?: Gym()
        }
    }
}

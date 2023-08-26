package com.example.gymapp.ui.screen.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymapp.data.repository.gym.GymRepository
import com.example.gymapp.model.Gym
import com.example.gymapp.ui.navigation.GYM_ID_ARGUMENT_NAME
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import com.example.gymapp.ui.screen.viewmodel.state.GymDetailsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
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
    val gymDetailsUiState = MutableStateFlow(GymDetailsUiState())
    private val gymId: String = checkNotNull(savedStateHandle[GYM_ID_ARGUMENT_NAME])
    init{
        getGymDetails()
    }
    private fun getGymDetails() {
        viewModelScope.launch {
            gymRepository.getGymById(gymId).collect{gym ->
                gymDetailsUiState.update { currentState ->
                    currentState.copy(
                        gym = gym ?: Gym()
                    )
                }
            }
        }
    }
}

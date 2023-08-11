package com.example.gymapp.ui.screen.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymapp.data.repository.GymRepository
import com.example.gymapp.model.Address
import com.example.gymapp.model.Gym
import com.example.gymapp.model.Location
import com.example.gymapp.model.Timings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
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
    savedStateHandle: SavedStateHandle, gymRepository: GymRepository
) : ViewModel() {

    private val gymId: Int = checkNotNull(savedStateHandle["gymId"])

    val gymDetailsUiState: StateFlow<GymDetailsUiState> =
        gymRepository.getGymDetailsWithId(gymId)
            .map { GymDetailsUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000L),
                initialValue = GymDetailsUiState()
            )

}

package com.example.gymapp.ui.screen.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymapp.data.repository.GymRepository
import com.example.gymapp.model.Address
import com.example.gymapp.model.Gym
import com.example.gymapp.model.Location
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

//sealed interface GymDetailsUiState {
//    data class Success(val amphibians: Gym) : GymDetailsUiState
//    object Error : GymDetailsUiState
//    object Loading : GymDetailsUiState
//}

data class GymDetailsUiState(
    val gym: Gym = Gym(
        1,
        "Cult Gym Rajajinagar 12th Cross",
        "Gym",
        Address(
            1,
            "3rd & 4th floor. 12th Cross Rd",
            "Mahalakshmi Layout",
            "Bengaluru",
            560010,
            Location(
                3.2323,
                4.232323
            )
        ),
        "This is the Gym Description",
        imgSrcLst = "https://developer.android.com/codelabs/basic-android-kotlin-compose-amphibians-app/img/great-basin-spadefoot.png"
    )
)

@HiltViewModel
class GymDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    val gymRepository: GymRepository
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

package com.example.gymapp.ui.screen.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymapp.data.repository.GymRepository
import com.example.gymapp.data.repository.OfflineGymRepository
import com.example.gymapp.model.Address
import com.example.gymapp.model.Gym
import com.example.gymapp.model.Location
import com.example.gymapp.model.PlannedActivitySchedule
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

data class GymDetailsUiState( // Create Gym object with None values
    val gym: Gym = Gym(
        1,
        "",
        "",
        listOf("Cardio", "Gym", "Zumba", "Yoga", "HIIT"),
        address = Address(
            1,
            "77, Ground Floor, Below Stories Pub",
            "Mahalakshmi Metro Nandini Layout",
            "Bengaluru",
            560010,
            Location(
                13.00868, 77.54906
            )
        ),
        "This is the Gym Description",
        imageUrls = "https://developer.android.com/codelabs/basic-android-kotlin-compose-amphibians-app/img/great-basin-spadefoot.png",
        plannedActivitySchedules = listOf(
            PlannedActivitySchedule("Mon", 6, 0, 22, 0),
            PlannedActivitySchedule("Tue", 6, 0, 22, 0),
            PlannedActivitySchedule("Wed", 6, 0, 22, 0),
            PlannedActivitySchedule("Thu", 6, 0, 22, 0),
            PlannedActivitySchedule("Fri", 6, 0, 22, 0),
            PlannedActivitySchedule("Sat", 6, 0, 22, 0),
            PlannedActivitySchedule("Sun", 6, 0, 22, 0)
        ),
        amenities = listOf("Parking", "CCTV", "Locker")
    )
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

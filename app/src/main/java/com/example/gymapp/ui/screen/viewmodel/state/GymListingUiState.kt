package com.example.gymapp.ui.screen.viewmodel.state

import com.example.gymapp.model.Gym
import com.example.gymapp.model.User

data class GymListingUiState (
    val gyms: List<Gym> = listOf()
)

data class GymDetailsUiState (
    val gym: Gym = Gym()
)

data class UserUiState (
    val user: User = User()
)

data class HomeScreenUiState (
    val gyms: List<Gym> = listOf(),
    val user: User = User()
)

package com.example.gymapp.ui.screen.viewmodel.state

import com.example.gymapp.model.Gym
import com.example.gymapp.model.GymFullTextSearch
import com.example.gymapp.model.User

data class GymListingUiState(
    val gymFullTextSearch: List<GymFullTextSearch> = listOf()
)

data class GymDetailsUiState (
    val gym: Gym = Gym()
)

data class UserUiState (
    val user: User = User()
)

data class HomeScreenUiState (
    val gymFullTextSearchList: List<GymFullTextSearch> = listOf(),
    val user: User = User()
)

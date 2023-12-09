package com.example.gymapp.ui.screen.viewmodel.state

import com.example.gymapp.model.Gym
import com.example.gymapp.model.GymFullTextSearchIndex
import com.example.gymapp.model.User

data class GymListingUiState(
    val gymFullTextSearchIndices: List<GymFullTextSearchIndex> = listOf()
)

data class GymDetailsUiState (
    val gym: Gym = Gym()
)

data class UserUiState (
    val user: User = User()
)

data class HomeScreenUiState (
    val gymFullTextSearchIndices: List<GymFullTextSearchIndex> = listOf(),
    val user: User = User()
)

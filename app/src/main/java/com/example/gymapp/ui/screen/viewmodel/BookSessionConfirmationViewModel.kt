package com.example.gymapp.ui.screen.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.gymapp.data.repository.gym.GymRepository
import javax.inject.Inject

class BookSessionConfirmationViewModel@Inject constructor(
    savedStateHandle: SavedStateHandle, val gymRepository: GymRepository
) : ViewModel()  {
}
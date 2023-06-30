package com.example.gymapp.ui.screen.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.gymapp.ui.screen.viewmodel.state.UserRegistrationUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

private val TAG = "User Registration View Model"

class UserRegistrationViewModel: ViewModel() {

    private val _userRegistrationUiState = MutableStateFlow(UserRegistrationUiState())
    val userRegistrationUiState: StateFlow<UserRegistrationUiState>  = _userRegistrationUiState.asStateFlow()

    fun setMobileNumber(userInput: String) {
        _userRegistrationUiState.update {currentState ->
            currentState.copy(
                mobileNumber = userInput
            )
        }
    }
    companion object {
        val factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                UserRegistrationViewModel()
            }
        }
    }
}
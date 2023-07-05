package com.example.gymapp.ui.screen.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.gymapp.data.repository.UserDetailRepository
import com.example.gymapp.ui.screen.viewmodel.state.SplashScreenUiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
private const val TAG = "Splash Screen View Model"
class SplashScreenViewModel(private val userDetailRepository: UserDetailRepository): ViewModel()
{
    val splashScreenUiState: StateFlow<SplashScreenUiState> = userDetailRepository.userRegistrationStatus.map { registrationState ->
        Log.d(TAG, "registration state ${registrationState}")
        SplashScreenUiState(userRegistrationState = registrationState, isLoadingDone = true)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        SplashScreenUiState()
    )

    companion object {
        val factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val userDetailRepository = facilityApplication().container.userDetailRepository
                SplashScreenViewModel(userDetailRepository)
            }
        }
    }
}
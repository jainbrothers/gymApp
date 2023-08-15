package com.example.gymapp.ui.screen.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymapp.data.repository.UserDetailRepository
import com.example.gymapp.ui.screen.viewmodel.state.SplashScreenUiState
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

private const val TAG = "Splash Screen View Model"
@HiltViewModel
class SplashScreenViewModel @Inject constructor(val userDetailRepository: UserDetailRepository): ViewModel() {
    val splashScreenUiState: StateFlow<SplashScreenUiState> = userDetailRepository.userRegistrationStatus.map { registrationState ->
        Log.d(TAG, "registration state ${registrationState}")
        SplashScreenUiState(userRegistrationState = registrationState,
            isLoadingDone = true,
            mobileNumber=userDetailRepository.userMobileNumber.first())
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        SplashScreenUiState()
    )


//    val splashScreenUiState: StateFlow<SplashScreenUiState> = userDetailRepository.map { userDetailRepo: UserDetailRepository ->
//        Log.d(TAG, "registration state ${userDetailRepo}")
//        SplashScreenUiState(
//            userRegistrationState = userDetailRepo.userRegistrationStatus.first(),
//            isLoadingDone = true,
//            mobileNumber = userDetailRepo.userMobileNumber.first()
//        )
//    }.stateIn(
//        viewModelScope,
//        SharingStarted.WhileSubscribed(5000L),
//        SplashScreenUiState()
//    )
}
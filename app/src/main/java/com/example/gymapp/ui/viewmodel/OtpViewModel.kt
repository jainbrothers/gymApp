package com.example.gymapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory

class OtpViewModel: ViewModel() {

    fun generateOtp(mobileNumber: String): String {
        return ""
    }

    companion object {
        val factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                OtpViewModel()
            }
        }
    }
}
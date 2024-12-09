package com.hamaro.rockettranslatenativeapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hamaro.rockettranslatenativeapp.ui.presentation.auth.AuthViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class MainViewModel(
    private val authViewModel: AuthViewModel
) : ViewModel() {

    val startDestination: StateFlow<Destination?> = authViewModel.currentUser
        .map { user ->
            user?.let {
                if (!user.isAnonymous)
                    Destination.Main
                else
                    Destination.Auth
            } ?: Destination.Auth
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            null
        )

    sealed class Destination(val route: String) {
        data object Auth : Destination("auth")
        data object Main : Destination("main")
    }
}


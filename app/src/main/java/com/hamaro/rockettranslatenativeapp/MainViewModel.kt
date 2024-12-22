package com.hamaro.rockettranslatenativeapp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hamaro.rockettranslatenativeapp.ui.presentation.auth.AuthViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(
    private val authViewModel: AuthViewModel
) : ViewModel() {

    var keepSplashScreen by mutableStateOf(true)
        private set

    init {
        viewModelScope.launch {
            snapshotFlow { startDestination }
                .filterNotNull()
                .collectLatest {
                    delay(100)
                    keepSplashScreen = false
                }
        }
    }

    val startDestination: StateFlow<Destination?> = authViewModel.currentUser
        .map { user ->
            user?.let {
                if (!user.isAnonymous)
                    Destination.Main
                else
                    Destination.Auth
            } ?: Destination.Splash
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            null
        )



    sealed class Destination(val route: String) {
        data object Auth : Destination("auth")
        data object Main : Destination("main")
        data object SignUp : Destination("signup")
        data object Splash : Destination("")
    }
}


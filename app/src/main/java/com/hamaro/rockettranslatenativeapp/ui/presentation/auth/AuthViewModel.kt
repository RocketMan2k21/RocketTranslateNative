package com.hamaro.rockettranslatenativeapp.ui.presentation.auth

import androidx.lifecycle.viewModelScope
import com.hamaro.rockettranslatenativeapp.domain.AuthService
import com.hamaro.rockettranslatenativeapp.domain.base.BaseViewModel
import com.hamaro.rockettranslatenativeapp.domain.model.UiState
import com.hamaro.rockettranslatenativeapp.domain.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class AuthViewModel(
    private val authService: AuthService
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    private val _emailError = MutableStateFlow(false)
    val emailError = _emailError.asStateFlow()

    private val _passwordError = MutableStateFlow(false)
    val passwordError = _passwordError.asStateFlow()

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser = _currentUser.asStateFlow()

    private val _isProcessing = MutableStateFlow(false)
    val isProcessing = _isProcessing.asStateFlow()

    private val _confirmPasswordError = MutableStateFlow(false)
    val confirmationPassword = _confirmPasswordError.asStateFlow()

    val isButtonEnabled: StateFlow<Boolean> = combine(uiState) { states ->
        val state = states.first()
        state.email.isNotBlank() && state.password.isNotBlank()
    }.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), false
    )

    private val _authState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val authState: StateFlow<UiState<Unit>> = _authState.asStateFlow()


    init {
        launchWithCatchingException {
            authService.currentUser.collect {
                _currentUser.value = it
            }
        }

        launchWithCatchingException {
            authService.authExceptionFlow
                .filterNot { it.isEmpty() }
                .collect{ errorMessage ->
                    println("Auth error msg: $errorMessage")
                    _isProcessing.value = false
                    _authState.value = UiState.Error(errorMessage)
                }
        }
    }

    fun onEmailChange(newValue: String) {
        _uiState.update { it.copy(email = newValue.trim()) }
        //reset error
        if (newValue.isNotBlank()) _emailError.value = false
    }

    fun onPasswordChange(newValue: String) {
        _uiState.update { it.copy(password = newValue.trim()) }
        //reset error
        if (newValue.isNotBlank()) _passwordError.value = false
    }

    fun onConfirmationPasswordChange(newValue : String) {
        _uiState.update { it.copy(confirmationPassword = newValue.trim()) }
        //reset error
        if (newValue.isNotBlank() && newValue == _uiState.value.password) _authState.value = UiState.Idle
    }

    fun onSignInClick() {
        _authState.value = UiState.Idle
        if (_uiState.value.email.isEmpty()) {
            _emailError.value = true
            return
        }

        if (_uiState.value.password.isEmpty()) {
            _passwordError.value = true
            return
        }

        _isProcessing.value = true
        launchWithCatchingException {
            try {
                authService.authenticate(_uiState.value.email, _uiState.value.password)
                _isProcessing.value = false
            } catch (e: Exception) {
                _isProcessing.value = false
            }
        }
    }

    fun onSignOut() {
        launchWithCatchingException {
            authService.signOut()
        }
    }

    fun createNewUserClick () {
        _authState.value = UiState.Idle
        if (_uiState.value.email.isEmpty()) {
            _emailError.value = true
            return
        }

        if (_uiState.value.password.isEmpty()) {
            _passwordError.value = true
            return
        }

        if (_uiState.value.confirmationPassword.isEmpty() || _uiState.value.confirmationPassword != _uiState.value.password) {
            _authState.value = UiState.Error("The passwords don't match")
            return
        }

        _isProcessing.value = true

        launchWithCatchingException {
            try {
                authService.createUser(_uiState.value.email, _uiState.value.password)
                _isProcessing.value = false
            } catch (e : Exception) {
                _isProcessing.value = false
            }
        }
    }

}

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val confirmationPassword : String = ""
)


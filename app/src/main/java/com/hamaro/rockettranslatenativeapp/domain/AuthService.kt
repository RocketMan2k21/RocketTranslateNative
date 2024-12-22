package com.hamaro.rockettranslatenativeapp.domain

import com.hamaro.rockettranslatenativeapp.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface AuthService {

    val currentUserId: String
    val isAuthenticated: Boolean

    val currentUser: Flow<User>
    val authExceptionFlow : Flow<String>
    suspend fun authenticate(email: String, password: String)
    suspend fun createUser(email: String, password: String)

    suspend fun signOut()
}
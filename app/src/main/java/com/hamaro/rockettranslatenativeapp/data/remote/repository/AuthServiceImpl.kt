package com.hamaro.rockettranslatenativeapp.data.remote.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthEmailException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
import com.hamaro.rockettranslatenativeapp.domain.AuthService
import com.hamaro.rockettranslatenativeapp.domain.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class AuthServiceImpl(
    val auth: FirebaseAuth,
    val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
): AuthService{

    override val currentUserId: String
        get() = auth.currentUser?.uid.toString()

    override val isAuthenticated: Boolean
        get() = auth.currentUser != null && auth.currentUser?.isAnonymous == false

    private val _authExceptionFlow = MutableSharedFlow<String>(replay = 0)
    override val authExceptionFlow: Flow<String> = _authExceptionFlow

    override val currentUser: Flow<User> = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener { auth ->
            val firebaseUser: FirebaseUser? = auth.currentUser
            val user = firebaseUser?.let {
                User(it.uid, it.isAnonymous)
            } ?: User()
            trySend(user)
        }

        auth.addAuthStateListener(authStateListener)

        awaitClose { auth.removeAuthStateListener(authStateListener) }
    }

    override suspend fun authenticate(email: String, password: String) {
        try {
            auth.signInWithEmailAndPassword(email, password).await()
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            _authExceptionFlow.emit("Invalid email or password")
            throw e
        } catch (e: Exception) {
            _authExceptionFlow.emit(e.message ?: "Authentication failed")
            throw e
        }
    }


    override suspend fun createUser(email: String, password: String) {
        try {
            auth.createUserWithEmailAndPassword(email, password)
        } catch (e : Exception) {
            _authExceptionFlow.emit(e.message ?: "Authentication failed")
            throw e
        }
    }

    override suspend fun signOut() {

        if (auth.currentUser?.isAnonymous == true) {
            auth.currentUser?.delete()
        }

        auth.signOut()
    }
}
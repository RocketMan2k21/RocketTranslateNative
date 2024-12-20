package com.hamaro.rockettranslatenativeapp.data.remote.repository

import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.hamaro.rockettranslatenativeapp.domain.AuthService
import com.hamaro.rockettranslatenativeapp.domain.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow

class AuthServiceImpl(
    val auth: FirebaseAuth,
    val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
): AuthService{

    override val currentUserId: String
        get() = auth.currentUser?.uid.toString()

    override val isAuthenticated: Boolean
        get() = auth.currentUser != null && auth.currentUser?.isAnonymous == false

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

    private suspend fun launchWithAwait(block : suspend  () -> Unit) {
        scope.async {
            block()
        }.await()
    }
    override suspend fun authenticate(email: String, password: String) {
       launchWithAwait {
            auth.signInWithEmailAndPassword(email, password)
        }
    }
    override suspend fun createUser(email: String, password: String) {
        val result = launchWithAwait {
            auth.createUserWithEmailAndPassword(email, password)
        }
    }

    override suspend fun signOut() {

        if (auth.currentUser?.isAnonymous == true) {
            auth.currentUser?.delete()
        }

        auth.signOut()
    }
}
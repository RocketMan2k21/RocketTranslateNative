package com.hamaro.rockettranslatenativeapp.ui.presentation.auth

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hamaro.rockettranslatenativeapp.domain.model.UiState
import com.hamaro.rockettranslatenativeapp.domain.model.User
import com.hamaro.rockettranslatenativeapp.ui.theme.Typography
import com.hamaro.rockettranslatenativeapp.ui.theme.grayBlack

@Composable
fun AuthContent(
    modifier: Modifier = Modifier,
    uiState: LoginUiState,
    authErrorState: UiState<Unit>,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSignInClick: () -> Unit,
    isProcessing: Boolean,
    currentUser: User?,
    isButtonEnabled : Boolean,
    isPasswordError: Boolean,
    isEmailError : Boolean,
    onSignOut: () -> Unit,
    onSignIn: () -> Unit,
    buttonText : String = "SIGN IN"
) {

    Box(
        modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(), value = uiState.email, label = {
                    Text("Email")
                }, onValueChange = onEmailChange,
                isError = isEmailError
            )

            Spacer(modifier = Modifier.height(16.dp))

            AnimatedVisibility(isEmailError) {
                Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start) {
                    Text("Invalid email", color = MaterialTheme.colorScheme.error)
                }
            }

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = uiState.password,
                visualTransformation = PasswordVisualTransformation(),
                label = {
                    Text("Password")
                },
                onValueChange = onPasswordChange,
                isError = isPasswordError
            )

            Spacer(modifier = Modifier.height(16.dp))

            AnimatedVisibility(isPasswordError) {
                Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start) {
                    Text("Invalid password", color = MaterialTheme.colorScheme.error)
                }
            }

            AnimatedContent(authErrorState, label = "") {
                when(it) {
                    is UiState.Error -> {
                        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start) {
                            Text(it.message, color = MaterialTheme.colorScheme.error)
                        }
                    }
                    UiState.Idle -> Unit
                    UiState.Loading -> Unit
                    is UiState.Success -> Unit
                }
            }
            
            Spacer(Modifier.height(10.dp))

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp), onClick = onSignInClick,
                enabled = isButtonEnabled,
                contentPadding = PaddingValues(4.dp)
            ) {
                if (isProcessing) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text(buttonText)
                }
            }


            Spacer(modifier = Modifier.height(16.dp))

            LaunchedEffect(currentUser) {
                Log.d("AuthScreen", "Try to sign in")

                if (currentUser != null && !currentUser.isAnonymous) {
                    Log.d("AuthScreen", "Logging in...")
                    onSignIn()
                }
            }
        }
    }
}


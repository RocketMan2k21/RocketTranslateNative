package com.hamaro.rockettranslatenativeapp.ui.presentation.auth

import android.util.Log
import android.widget.Space
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hamaro.rockettranslatenativeapp.domain.model.UiState
import com.hamaro.rockettranslatenativeapp.domain.model.User
import com.hamaro.rockettranslatenativeapp.ui.theme.Typography
import com.hamaro.rockettranslatenativeapp.ui.theme.grayBlack
import org.koin.androidx.compose.koinViewModel

@Composable
fun SignUpScreen(
    viewModel: AuthViewModel = koinViewModel(),
    navigateToAuthScreen : () -> Unit,
    onSuccessSignIn : () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val emailError by viewModel.emailError.collectAsState()
    val passwordError by viewModel.passwordError.collectAsState()
    val isProcessing by viewModel.isProcessing.collectAsState()
    val isButtonEnabled by viewModel.isButtonEnabled.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    val authState by viewModel.authState.collectAsState()
    val confirmPasswordError by  viewModel.confirmationPassword.collectAsState()

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {

        Text(text = "Sign up", style = Typography.headlineSmall, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)

        SignUpContent (
            uiState = uiState,
            onEmailChange = viewModel::onEmailChange,
            onPasswordChange = viewModel::onPasswordChange,
            onSignInClick = { viewModel.createNewUserClick()},
            isProcessing = isProcessing,
            currentUser = currentUser,
            isButtonEnabled = isButtonEnabled,
            authErrorState = authState,
            isPasswordError = passwordError,
            isEmailError = emailError,
            onSignOut = viewModel::onSignOut,
            onSignIn = onSuccessSignIn,
            buttonText = "CREATE ACCOUNT",
            onConfirmationPasswordChange = viewModel::onConfirmationPasswordChange,
            confirmPasswordError = confirmPasswordError
        )

        AuthClickable {
            navigateToAuthScreen()
        }
    }
}

@Composable
fun AuthClickable(
    navigateToAuthScreen : () -> Unit
) {
    Column(
        Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            textAlign = TextAlign.Center,
            text = "Already have an account?",
            style = Typography.bodyLarge,
            color = grayBlack
        )

        Text(
            modifier = Modifier
                .clickable { navigateToAuthScreen() },
            textAlign = TextAlign.Center,
            text = "Sign In",
            color = MaterialTheme.colorScheme.primary,
            style = Typography.labelLarge,
            textDecoration = TextDecoration.Underline
        )
    }

}

@Preview
@Composable
fun PreviewAuthClickable(){
    AuthClickable{}
}

@Composable
fun SignUpContent(
    modifier: Modifier = Modifier,
    uiState: LoginUiState,
    authErrorState: UiState<Unit>,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmationPasswordChange : (String) -> Unit,
    onSignInClick: () -> Unit,
    isProcessing: Boolean,
    currentUser: User?,
    isButtonEnabled : Boolean,
    isPasswordError: Boolean,
    isEmailError : Boolean,
    onSignOut: () -> Unit,
    onSignIn: () -> Unit,
    confirmPasswordError : Boolean,
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

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = uiState.confirmationPassword,
                visualTransformation = PasswordVisualTransformation(),
                label = {
                    Text("Confirm password")
                },
                onValueChange = onConfirmationPasswordChange,
                isError = confirmPasswordError
            )

            Spacer(Modifier.height(10.dp))

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



//            AnimatedVisibility(confirmPasswordError) {
//                Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start) {
//                    Text("The passwords don't match", color = MaterialTheme.colorScheme.error)
//                }
//            }

            Spacer(Modifier.height(16.dp))
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
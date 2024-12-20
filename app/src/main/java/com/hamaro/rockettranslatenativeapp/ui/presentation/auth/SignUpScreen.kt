package com.hamaro.rockettranslatenativeapp.ui.presentation.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {

        Text(text = "Sign up", style = Typography.headlineSmall, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)

        AuthContent (
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
            buttonText = "CREATE ACCOUNT"
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
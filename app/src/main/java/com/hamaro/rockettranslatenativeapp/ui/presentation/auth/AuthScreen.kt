package com.hamaro.rockettranslatenativeapp.ui.presentation.auth

import android.util.Log
import android.widget.Space
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import org.koin.androidx.compose.koinViewModel


@Composable
fun AuthScreen(
    viewModel: AuthViewModel = koinViewModel(),
    onSuccessSignIn: () -> Unit,
    navigateToSignUpPage: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsState()
    val emailError by viewModel.emailError.collectAsState()
    val passwordError by viewModel.passwordError.collectAsState()
    val isProcessing by viewModel.isProcessing.collectAsState()
    val isButtonEnabled by viewModel.isButtonEnabled.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    val authState by viewModel.authState.collectAsState()

    Column (
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier
                .height(100.dp)
                .fillMaxWidth()
            ,
            text = "Welcome to Rocket Translate!",
            textAlign = TextAlign.Center,
            fontSize = 24.sp
        )

        Spacer(Modifier.height(20.dp))

        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = "Sign In",
            textAlign = TextAlign.Center,
            fontSize = 24.sp
        )

        Spacer(Modifier.height(20.dp))

        AuthContent(
            uiState = uiState,
            onEmailChange = viewModel::onEmailChange,
            onPasswordChange = viewModel::onPasswordChange,
            onSignInClick = { viewModel.onSignInClick() },
            isProcessing = isProcessing,
            currentUser = currentUser,
            isButtonEnabled = isButtonEnabled,
            authErrorState = authState,
            isPasswordError = passwordError,
            isEmailError = emailError,
            onSignOut = viewModel::onSignOut,
            onSignIn = onSuccessSignIn
        )

       SignUpClickable{ navigateToSignUpPage() }
    }
}


@Composable
fun SignUpClickable(
    modifier: Modifier = Modifier,
    navigateToSignUpScreen : () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            textAlign = TextAlign.Center,
            text = "Don't have any account?",
            style = Typography.bodyLarge,
            color = grayBlack
        )

        Text(
            modifier = Modifier
                .clickable { navigateToSignUpScreen() },
            textAlign = TextAlign.Center,
            text = "Sign Up",
            color = MaterialTheme.colorScheme.primary,
            style = Typography.labelLarge,
            textDecoration = TextDecoration.Underline
        )
    }

}

@Preview
@Composable
fun PreviewSignUp(){
    SignUpClickable(){}
}



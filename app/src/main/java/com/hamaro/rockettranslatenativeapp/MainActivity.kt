package com.hamaro.rockettranslatenativeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.initialize
import com.hamaro.rockettranslatenativeapp.di.initializeKoin
import com.hamaro.rockettranslatenativeapp.ui.navigation.SetupNavigation
import com.hamaro.rockettranslatenativeapp.ui.theme.RocketTranslateNativeTheme
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Firebase.initialize(this)
        enableEdgeToEdge()
        initializeKoin()

        setContent {
            RocketTranslateNativeTheme {
                val viewModel : MainViewModel = koinViewModel()
                val navController = rememberNavController()

                val startDestination by viewModel.startDestination.collectAsState()

                SetupNavigation(
                    navController,
                    startDestination = startDestination
                )
            }
        }
    }
}

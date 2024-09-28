package com.example.androidfirebase


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.androidfirebase.ui.theme.AndroidFireBaseTheme
import com.google.firebase.FirebaseApp

import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument


import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        enableEdgeToEdge()

        setContent {
            val navController = rememberNavController()
            val currentUser = FirebaseAuth.getInstance().currentUser

            // Si hay un usuario autenticado, redirige a la pantalla de inicio
            val startDestination = if (currentUser != null) {
                Routes.homeS
            } else {
                Routes.loginS
            }

            NavHost(navController = navController, startDestination = startDestination) {
                composable(Routes.loginS) {
                    LoginScreen(navController)
                }
                composable(Routes.homeS) {
                    HomeScreen(navController)
                }
                composable(Routes.registerS) {
                    RegisterScreen(navController)
                }
                composable(Routes.recoverPasswordS) {
                    RecoverPasswordScreen(navController)
                }

                composable(Routes.profileS) {
                    ProfileScreen(navController)
                }

                composable(Routes.locationS) {
                    LocationScreen(navController)
                }
            }
        }
    }
}



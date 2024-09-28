package com.example.androidfirebase

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    var showSnackbar by remember { mutableStateOf(false) }

    if (showSnackbar) {
        LaunchedEffect(snackbarHostState) {
            snackbarHostState.showSnackbar("Correo o contraseña incorrectos")
            showSnackbar = false
        }
    }

    val auth = FirebaseAuth.getInstance() // Obtiene la instancia de FirebaseAuth

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(id = R.drawable.login),
                contentDescription = "Login image",
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
                    .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Texto de bienvenida más grande y en negrita
            Text(
                text = "Bienvenido",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontSize = 28.sp, // Tamaño mayor para una lectura más clara
                    fontWeight = FontWeight.Bold // Negrita para mayor énfasis
                ),
                color = MaterialTheme.colorScheme.primary
            )

            // Subtexto de inicio de sesión con tamaño y color accesible
            Text(
                text = "Inicie sesión en su cuenta",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 20.sp, // Aumentar tamaño de letra
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f) // Mayor claridad
                )
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Campo de texto de correo electrónico con tamaño de letra más grande
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = {
                    Text(
                        "Correo electrónico",
                        fontSize = 20.sp, // Aumentar tamaño de etiqueta
                        fontWeight = FontWeight.Bold // Hacer la etiqueta más clara
                    )
                },
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 22.sp, // Texto dentro del campo más grande
                    color = Color.Black // Buen contraste para legibilidad
                ),
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f)
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo de texto de contraseña con las mismas mejoras de accesibilidad
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = {
                    Text(
                        "Contraseña",
                        fontSize = 20.sp, // Aumentar tamaño de etiqueta
                        fontWeight = FontWeight.Bold // Hacer la etiqueta más clara
                    )
                },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 22.sp, // Texto dentro del campo más grande
                    color = Color.Black // Buen contraste para legibilidad
                ),
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f)
                )
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Botón de acceso con texto más grande y centrado
            Button(
                onClick = {
                    // Autenticación con Firebase
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                // Inicio de sesión exitoso
                                val user: FirebaseUser? = auth.currentUser
                                navController.navigate("home_S")
                            } else {
                                // Fallo en la autenticación
                                showSnackbar = true
                            }
                        }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    "Acceso",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 20.sp // Texto más grande
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Texto de registro con tamaño más grande
            TextButton(
                onClick = { navController.navigate("register_S") },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(
                    "¿No tienes una cuenta? Regístrate",
                    fontSize = 18.sp, // Aumentar tamaño del texto
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Texto para la recuperación de contraseña con mejoras en accesibilidad
            TextButton(
                onClick = { navController.navigate("recover_password_S") }, // Navegar a la pantalla de recuperación
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(
                    "¿Olvidaste tu contraseña?",
                    fontSize = 18.sp, // Aumentar tamaño del texto
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Snackbar para notificaciones en la parte inferior
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }

}


package com.example.androidfirebase

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecoverPasswordScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    val auth = FirebaseAuth.getInstance() // Instancia de FirebaseAuth
    val snackbarHostState = remember { SnackbarHostState() }
    var showSnackbar by remember { mutableStateOf(false) }

    if (showSnackbar) {
        LaunchedEffect(snackbarHostState) {
            snackbarHostState.showSnackbar("Si el correo existe, se ha enviado un enlace de recuperación")
            showSnackbar = false
        }
    }

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
            // Título más grande y negrita
            Text(
                text = "Recuperar Contraseña",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontSize = 28.sp, // Aumentar tamaño de texto
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Subtítulo accesible y claro
            Text(
                text = "Ingrese su correo electrónico para recibir un enlace de recuperación.",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 20.sp, // Texto más grande para mayor accesibilidad
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f) // Contraste claro
                )
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Campo de entrada para el correo electrónico
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = {
                    Text(
                        "Correo electrónico",
                        fontSize = 20.sp, // Aumentar el tamaño del texto de la etiqueta
                        fontWeight = FontWeight.Bold
                    )
                },
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 22.sp, // Texto dentro del campo más grande para accesibilidad
                    color = Color.Black // Contraste claro para el texto
                ),
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f)
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botón de enviar enlace de recuperación con texto más grande
            Button(
                onClick = {
                    // Enviar enlace de recuperación de contraseña
                    auth.sendPasswordResetEmail(email)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
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
                    "Recuperar Contraseña",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 20.sp // Aumentar tamaño del texto para el botón
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón para regresar al login
            TextButton(
                onClick = { navController.navigate("login_S") },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(
                    "Volver al inicio de sesión",
                    fontSize = 18.sp, // Aumentar tamaño del texto para mayor visibilidad
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        // Snackbar para mostrar mensajes en la parte inferior de la pantalla
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }

}

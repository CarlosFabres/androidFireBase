package com.example.androidfirebase

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource

import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

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
import com.google.firebase.auth.FirebaseUser

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {
    val auth = FirebaseAuth.getInstance()
    val userId = auth.currentUser?.uid
    val database = FirebaseDatabase.getInstance().getReference("users")

    // Variables para los datos del usuario
    var username by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var showSnackbar by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    // Cargar datos del usuario
    LaunchedEffect(userId) {
        userId?.let {
            database.child(it).get().addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    username = snapshot.child("username").value.toString()
                    phoneNumber = snapshot.child("phoneNumber").value.toString()
                }
            }.addOnFailureListener {
                showSnackbar = true
            }
        }
    }

    if (showSnackbar) {
        LaunchedEffect(snackbarHostState) {
            snackbarHostState.showSnackbar("Error al cargar datos del usuario")
            showSnackbar = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally // Alineación centrada horizontalmente
        ) {

            Image(
                painter = painterResource(id = R.drawable.ic_user),
                contentDescription = "Login image",
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
                    .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
            )

            // Texto de perfil con tamaño de letra más grande y en negrita
            Text(
                text = "Perfil",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontSize = 28.sp, // Aumenta el tamaño de letra para mayor claridad
                    fontWeight = FontWeight.Bold // Hacerlo más claro y visible
                ),
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Campo de texto más grande y con un texto más claro
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = {
                    Text(
                        "Nombre de usuario",
                        fontSize = 20.sp, // Aumentar tamaño de etiqueta
                        fontWeight = FontWeight.Bold // Hacer la etiqueta más clara
                    )
                },
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 22.sp, // Texto dentro del campo más grande
                    color = Color.Black // Asegurar que el texto sea claramente visible
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Segundo campo de texto con configuraciones similares
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                label = {
                    Text(
                        "Número de teléfono",
                        fontSize = 20.sp, // Aumentar tamaño de etiqueta
                        fontWeight = FontWeight.Bold // Hacer la etiqueta más clara
                    )
                },
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 22.sp, // Texto dentro del campo más grande
                    color = Color.Black // Asegurar que el texto sea claramente visible
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Botón para actualizar datos con texto más grande
            Button(
                onClick = {
                    userId?.let {
                        database.child(it).updateChildren(mapOf(
                            "username" to username,
                            "phoneNumber" to phoneNumber
                        )).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                // Datos actualizados con éxito
                                navController.popBackStack() // Regresar a la pantalla anterior
                            } else {
                                // Fallo al actualizar datos
                                showSnackbar = true
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Actualizar datos", fontSize = 20.sp) // Aumentar tamaño del texto del botón
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón para cerrar sesión con texto más grande
            Button(
                onClick = {
                    FirebaseAuth.getInstance().signOut()
                    navController.navigate("login_S") {
                        popUpTo("home_S") { inclusive = true }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Cerrar sesión", fontSize = 20.sp) // Aumentar tamaño del texto del botón
            }
        }

        // Snackbar para mostrar mensajes en la parte inferior
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter) // Alineación al centro en la parte inferior
        )
    }

}


package com.example.androidfirebase

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.content.ActivityNotFoundException
import android.content.Intent
import android.speech.tts.TextToSpeech.OnInitListener
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import java.util.Locale

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.filled.LocationOn

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color

@Composable
fun HomeScreen(navController: NavController) {
    val context = LocalContext.current
    var username by remember { mutableStateOf("") }
    var userInput by remember { mutableStateOf("") }
    var textToSpeech: TextToSpeech? = remember { null }
    var speechRecognizerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            if (!data.isNullOrEmpty()) {
                userInput = data[0] // Obtiene el texto del reconocimiento
            }
        }
    }

    // Inicializa TTS
    if (textToSpeech == null) {
        textToSpeech = TextToSpeech(context) { status ->
            if (status != TextToSpeech.SUCCESS) {
                // Manejo de errores
            }
        }
        textToSpeech?.language = Locale.getDefault()
    }

    // Código para obtener el usuario de Firebase (igual que antes)
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    if (userId != null) {
        val database = FirebaseDatabase.getInstance().getReference("users").child(userId)
        LaunchedEffect(userId) {
            database.child("username").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val retrievedUsername = dataSnapshot.getValue(String::class.java)
                    if (retrievedUsername != null) {
                        username = retrievedUsername
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e("HomeScreen", "Error al obtener el username", databaseError.toException())
                }
            })
        }
    }

    // Interfaz de usuario
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(id = R.drawable.logo_home),
            contentDescription = "Login image",
            modifier = Modifier.size(200.dp)
        )

        Text(
            text = "Bienvenido, $username",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontSize = 28.sp, // Aumenta el tamaño del texto
                fontWeight = FontWeight.Bold // Aumenta el peso para mayor claridad
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.navigate(Routes.profileS) },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Perfil", fontSize = 20.sp) // Aumenta el tamaño del texto del botón
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Campo de texto más grande y claro
        OutlinedTextField(
            value = userInput,
            onValueChange = { userInput = it },
            label = {
                Text(
                    "Escribe algo...",
                    fontSize = 20.sp, // Tamaño del texto de la etiqueta
                    fontWeight = FontWeight.Bold, // Hacer que la etiqueta sea más clara
                    color = Color.Black // Puedes cambiar el color para mayor contraste
                )
            },
            textStyle = LocalTextStyle.current.copy(
                fontSize = 24.sp, // Tamaño del texto dentro del campo
                color = Color.Black // Color del texto dentro del campo
            ),
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text) // Mantiene el teclado para texto
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para reproducir texto
        Button(
            onClick = {
                textToSpeech?.speak(userInput, TextToSpeech.QUEUE_FLUSH, null, null)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.PlayArrow,
                contentDescription = "Reproducir texto",
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Reproducir texto", fontSize = 20.sp) // Aumenta el tamaño del texto del botón
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para reconocimiento de voz
        Button(
            onClick = {
                val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                    putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                    putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
                }
                try {
                    speechRecognizerLauncher.launch(intent)
                } catch (e: ActivityNotFoundException) {
                    Toast.makeText(context, "No se encontró ningún servicio de reconocimiento de voz", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.speak),
                contentDescription = "Hablar",
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Hablar", fontSize = 20.sp) // Aumenta el tamaño del texto del botón
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.navigate(Routes.locationS) },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.LocationOn,
                contentDescription = "Ir a geolocalización",
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Ubicación", fontSize = 20.sp) // Aumenta el tamaño del texto del botón
        }
    }
}



package com.example.androidfirebase

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.navigation.NavController
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@Composable
fun LocationScreen(navController: NavController) {
    val context = LocalContext.current

    // Estado para la ubicación del usuario
    var userLocation by remember { mutableStateOf<Location?>(null) }

    // Solicitar permisos de ubicación
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                getUserLocation(context) { location ->
                    userLocation = location
                }
            }
        }
    )

    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
            == PermissionChecker.PERMISSION_GRANTED) {
            getUserLocation(context) { location ->
                userLocation = location
            }
        } else {
            launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    // Si ya tenemos la ubicación del usuario, mostrar el mapa
    userLocation?.let { location ->
        val userLatLng = LatLng(location.latitude, location.longitude)
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(userLatLng, 15f) // Inicializar la posición de la cámara
        }

        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {
            // Mostrar marcador en la ubicación del usuario
            Marker(
                state = MarkerState(position = userLatLng),
                title = "Tu ubicación"
            )
        }

        // Centrar la cámara en la ubicación del usuario
        LaunchedEffect(userLatLng) {
            cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(userLatLng, 15f))
        }
    }
}

@SuppressLint("MissingPermission")
private fun getUserLocation(context: Context, onLocationUpdate: (Location) -> Unit) {
    val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    val locationRequest = LocationRequest.create().apply {
        interval = 10000
        fastestInterval = 5000
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    fusedLocationProviderClient.requestLocationUpdates(
        locationRequest,
        object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let { location ->
                    onLocationUpdate(location)
                }
            }
        },
        Looper.getMainLooper()
    )
}

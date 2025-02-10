package com.moqochallenge.poi.presentation.ui


import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.moqochallenge.poi.presentation.navigation.Screen
import com.moqochallenge.poi.presentation.viewmodel.POIViewModel
import androidx.hilt.navigation.compose.hiltViewModel

@SuppressLint("MissingPermission")
@Composable
fun POIMapScreen(viewModel: POIViewModel = hiltViewModel(), navController: NavController) {
    val pois by viewModel.poiList.collectAsState()

    val defaultLocation = LatLng(52.5200, 13.4050) // Default to Berlin
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(defaultLocation, 10f)
    }

    LaunchedEffect(Unit) {
        // Load POIs for the current bounding box
        viewModel.loadPOIs(51.648968, 7.4278984, 49.28752, 5.3754444)
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        pois.forEach { poi ->
            Marker(
                state = MarkerState(position = LatLng(poi.latitude, poi.longitude)),
                title = poi.name,
                onClick = {
                    navController.navigate(Screen.DetailScreen.createRoute(poi.id.toString()))
                    true
                }
            )
        }
    }
}
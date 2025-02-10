package com.moqochallenge.poi.presentation.ui


import android.annotation.SuppressLint
import android.text.Layout
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.moqochallenge.poi.presentation.navigation.Screen
import com.moqochallenge.poi.presentation.viewmodel.POIViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState


@SuppressLint("MissingPermission")
@Composable
fun POIMapScreen(viewModel: POIViewModel = hiltViewModel(), navController: NavController) {
    val pois by viewModel.poiList.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()

    val defaultLocation = LatLng(52.5200, 13.4050) // Default to Berlin
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(defaultLocation, 10f)
    }

    var lastBoundingBox by remember { mutableStateOf<Pair<LatLng, LatLng>?>(null) }

    // Refresh function
    fun refreshPOIs() {
        val visibleRegion = cameraPositionState.projection?.visibleRegion
        visibleRegion?.let {
            val ne = it.latLngBounds.northeast
            val sw = it.latLngBounds.southwest

            // Only refresh if bounding box has changed
            if (lastBoundingBox != Pair(ne, sw)) {
                lastBoundingBox = Pair(ne, sw)
                viewModel.loadPOIs(ne.latitude, ne.longitude, sw.latitude, sw.longitude)
            }
        }
    }

    // Observe map movement to trigger refresh
    LaunchedEffect(cameraPositionState.isMoving) {
        if (!cameraPositionState.isMoving) {
            refreshPOIs()
        }
    }

    // Swipe Refresh Layout
    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing),
        onRefresh = { refreshPOIs() }
    ) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {
            pois.forEach { poi ->
                Marker(
                    state = MarkerState(position = LatLng(poi.latitude, poi.longitude)),
                    title = poi.name,
                    onClick = {
                        navController.navigate(Screen.DetailScreen.createRoute(poi.id!!.toString()))
                        true
                    }
                )
            }
        }
    }

    // Floating Action Button for Manual Refresh
    Box(modifier = Modifier.fillMaxSize()) {
        FloatingActionButton(
            onClick = { refreshPOIs() },
            modifier = Modifier.align(Alignment.BottomEnd).padding(end = 16.dp, bottom = 100.dp)
        ) {
            Icon(imageVector = Icons.Default.Refresh, contentDescription = "Refresh POIs")
        }
    }
}
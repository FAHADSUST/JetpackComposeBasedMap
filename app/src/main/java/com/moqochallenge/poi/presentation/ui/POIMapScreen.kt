package com.moqochallenge.poi.presentation.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.moqochallenge.poi.presentation.navigation.Screen
import com.moqochallenge.poi.presentation.viewmodel.POIViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.flow.firstOrNull

@SuppressLint("MissingPermission")
@Composable
fun POIMapScreen(viewModel: POIViewModel = hiltViewModel(), navController: NavController?) {
    val pois by viewModel.poiList.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    val defaultLocation = LatLng(52.5200, 13.4050) // Default to Berlin
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(defaultLocation, 10f)
    }

    var lastBoundingBox by remember { mutableStateOf<Pair<LatLng, LatLng>?>(null) }

    fun refreshPOIs() {
        val visibleRegion = cameraPositionState.projection?.visibleRegion

        if (visibleRegion == null) {
            val boundingBox = viewModel.calculateBoundingBox(defaultLocation, 50.0) // 50km in each direction
            val ne = boundingBox.first
            val sw = boundingBox.second
            viewModel.loadPOIs(ne.latitude, ne.longitude, sw.latitude, sw.longitude)
        } else {
            val ne = visibleRegion.latLngBounds.northeast
            val sw = visibleRegion.latLngBounds.southwest

            if (lastBoundingBox != Pair(ne, sw)) {
                lastBoundingBox = Pair(ne, sw)
                viewModel.loadPOIs(ne.latitude, ne.longitude, sw.latitude, sw.longitude)
            }
        }
    }

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
        Box(modifier = Modifier.fillMaxSize()) {
            GoogleMap(
                modifier = Modifier.fillMaxSize().testTag("GoogleMap"),
                cameraPositionState = cameraPositionState
            ) {
                pois.forEach { poi ->
                    poi.id?.let { poiId ->
                        Marker(
                            state = MarkerState(position = LatLng(poi.latitude, poi.longitude)),
                            title = poi.name,
                            snippet = "POI_Marker_$poiId",  // Unique contentDescription for testing
                            onClick = {
                                navController?.navigate(Screen.DetailScreen.createRoute(poiId))
                                true
                            }
                        )
                    }
                }
            }

            // Show loading indicator while fetching data
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .testTag("LoadingIndicator")
                )
            }

            // Show "No POIs Available" message when data is empty
            if (!isLoading && pois.isEmpty()) {
                Text(
                    text = "No POIs Available",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .testTag("No_POIs_Text")
                )
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        FloatingActionButton(
            onClick = { refreshPOIs() },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 100.dp)
                .testTag("Refresh_Button")
        ) {
            Icon(imageVector = Icons.Default.Refresh, contentDescription = "Refresh POIs")
        }
    }
}

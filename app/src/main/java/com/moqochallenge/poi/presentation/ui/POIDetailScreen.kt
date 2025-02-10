package com.moqochallenge.poi.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.moqochallenge.poi.presentation.viewmodel.POIDetailViewModel
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun POIDetailScreen(poiId: String, navController: NavController, viewModel: POIDetailViewModel = hiltViewModel()) {
    val poi = viewModel.getPOIDetails(poiId).collectAsState(initial = null).value

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("POI Details") }, navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                }
            })
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            poi?.let {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = it.name, style = MaterialTheme.typography.h4)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = "Vehicle Type: ${it.vehicleType ?: "Unknown"}")
                    Text(text = "Position Type: ${it.positionType ?: "Unknown"}")
                    Text(text = "App Relation: ${it.appRelation}")
                }
            } ?: CircularProgressIndicator()
        }
    }
}
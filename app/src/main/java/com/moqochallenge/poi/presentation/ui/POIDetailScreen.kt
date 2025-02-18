package com.moqochallenge.poi.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.moqochallenge.poi.presentation.viewmodel.POIDetailViewModel
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun POIDetailScreen(poiId: String, navController: NavController?, viewModel: POIDetailViewModel = hiltViewModel()) {
    val poi by viewModel.poiDetail.collectAsState()

    LaunchedEffect(poiId) {
        viewModel.loadPOIDetails(poiId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("POI Details") },
                navigationIcon = {
                    IconButton(
                        onClick = { navController?.popBackStack() },
                        modifier = Modifier.testTag("Back_Button") // Added test tag for testing
                    ) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .testTag("POI_Detail_Screen"), // Added test tag for UI test
            contentAlignment = Alignment.Center
        ) {
            poi?.let {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = it.name, style = MaterialTheme.typography.h4, modifier = Modifier.testTag("POI_Name")) // Testable
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = "Vehicle Type: ${it.vehicleType ?: "Unknown"}", modifier = Modifier.testTag("POI_Vehicle_Type")) // Testable
                    Text(text = "Position Type: ${it.positionType ?: "Unknown"}", modifier = Modifier.testTag("POI_Position_Type")) // Testable
                    Text(text = "App Relation: ${it.appRelation}", modifier = Modifier.testTag("POI_App_Relation")) // Testable
                }
            } ?: CircularProgressIndicator()
        }
    }
}

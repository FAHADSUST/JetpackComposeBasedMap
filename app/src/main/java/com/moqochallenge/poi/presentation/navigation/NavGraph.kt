package com.moqochallenge.poi.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.moqochallenge.poi.presentation.ui.POIDetailScreen
import com.moqochallenge.poi.presentation.ui.POIMapScreen

sealed class Screen(val route: String) {
    object MapScreen : Screen("map_screen")
    object DetailScreen : Screen("detail_screen/{poiId}") {
        fun createRoute(poiId: String) = "detail_screen/$poiId"
    }
}

@Composable
fun AppNavigation(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = Screen.MapScreen.route) {
        composable(Screen.MapScreen.route) {
            POIMapScreen(navController = navController)
        }
        composable(Screen.DetailScreen.route) { backStackEntry ->
            val poiId = backStackEntry.arguments?.getString("poiId") ?: ""
            POIDetailScreen(poiId, navController)
        }
    }
}
package com.moqochallenge.poi.ui

import dagger.hilt.android.testing.HiltAndroidTest
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.moqochallenge.poi.MainActivity
import com.moqochallenge.poi.presentation.navigation.AppNavigation
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class POIComposeUITest {


    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    // Test if Map Screen Loads and POIs are Displayed
    @Test
    fun testMapScreenDisplaysPOIs() {
        composeTestRule.waitUntil(timeoutMillis = 10000) {
            composeTestRule.onAllNodesWithTag("GoogleMap").fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onNodeWithTag("GoogleMap").assertIsDisplayed()
    }

    //  Test Click on POI Marker Navigates to Detail Screen
    @Test
    fun testClickPOIMarkerOpensDetailScreen() {
        composeTestRule.waitUntil(timeoutMillis = 10000) {
            composeTestRule.onAllNodesWithTag("GoogleMap").fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithTag("GoogleMap").performClick() // Simulate map click
        composeTestRule.waitUntil(timeoutMillis = 10000) {
            composeTestRule.onAllNodesWithTag("POI_Detail_Screen").fetchSemanticsNodes()
                .isNotEmpty()
        }

        composeTestRule.onNodeWithTag("POI_Detail_Screen").assertIsDisplayed()
    }

}


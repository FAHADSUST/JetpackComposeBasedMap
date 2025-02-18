package com.moqochallenge.poi.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.moqochallenge.poi.data.model.POI
import com.moqochallenge.poi.data.repository.POIRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.cos

@HiltViewModel
class POIViewModel @Inject constructor(private val repository: POIRepository) : ViewModel() {

    private val _poiList = MutableStateFlow<List<POI>>(emptyList())
    val poiList: StateFlow<List<POI>> = _poiList.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    private val _isLoading = MutableStateFlow(false) // Added loading state
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null) // Error handling
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    /**
     * Load POIs based on the given bounding box.
     */
    fun loadPOIs(neLat: Double, neLng: Double, swLat: Double, swLng: Double) {
        val boundingBox = """{"ne_lat":$neLat,"ne_lng":$neLng,"sw_lat":$swLat,"sw_lng":$swLng}"""

        viewModelScope.launch {
            _isRefreshing.value = true
            _isLoading.value = true
            _errorMessage.value = null // Reset errors

            try {
                val pois = repository.fetchPOIsFromApi(boundingBox)
                _poiList.value = pois
            } catch (e: Exception) {
                _errorMessage.value = "Failed to load POIs: ${e.message}"
            } finally {
                _isRefreshing.value = false
                _isLoading.value = false
            }
        }
    }

    /**
     * Calculate bounding box from a given location and distance.
     */
    fun calculateBoundingBox(location: LatLng, distanceInKm: Double): Pair<LatLng, LatLng> {
        val earthRadius = 6371.0

        val lat = location.latitude
        val lng = location.longitude

        val deltaLat = distanceInKm / earthRadius
        val deltaLng = distanceInKm / (earthRadius * cos(Math.toRadians(lat)))

        val neLat = lat + Math.toDegrees(deltaLat)
        val neLng = lng + Math.toDegrees(deltaLng)
        val swLat = lat - Math.toDegrees(deltaLat)
        val swLng = lng - Math.toDegrees(deltaLng)

        return Pair(LatLng(neLat, neLng), LatLng(swLat, swLng))
    }
}

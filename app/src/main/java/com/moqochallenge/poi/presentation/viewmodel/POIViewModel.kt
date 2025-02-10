package com.moqochallenge.poi.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moqochallenge.poi.data.model.POI
import com.moqochallenge.poi.data.repository.POIRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class POIViewModel @Inject constructor(private val repository: POIRepository) : ViewModel() {

    private val _poiList = MutableStateFlow<List<POI>>(emptyList())
    val poiList: StateFlow<List<POI>> = _poiList

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    fun loadPOIs(neLat: Double, neLng: Double, swLat: Double, swLng: Double) {
        val boundingBox = """{"ne_lat":$neLat,"ne_lng":$neLng,"sw_lat":$swLat,"sw_lng":$swLng}"""
        viewModelScope.launch {
            _isRefreshing.value = true
            val pois = repository.fetchPOIsFromApi(boundingBox)
            _poiList.value = pois
            _isRefreshing.value = false
        }
    }
}
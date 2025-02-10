package com.moqochallenge.poi.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moqochallenge.poi.data.model.POI
import com.moqochallenge.poi.data.repository.POIRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class POIDetailViewModel @Inject constructor(private val repository: POIRepository) : ViewModel() {

    private val _poiDetail = MutableStateFlow<POI?>(null)
    val poiDetail: StateFlow<POI?> = _poiDetail.asStateFlow()

    fun loadPOIDetails(poiId: String) {

        viewModelScope.launch {
            _poiDetail.value = repository.fetchPOIById(poiId)
            Log.d("POIDetailViewModel", "loadPOIDetails: ${_poiDetail.value}")
        }
    }
}
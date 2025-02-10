package com.moqochallenge.poi.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.moqochallenge.poi.data.model.POI
import com.moqochallenge.poi.data.repository.POIRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltViewModel
class POIDetailViewModel @Inject constructor(private val repository: POIRepository) : ViewModel() {

    fun getPOIDetails(poiId: String): Flow<POI?> = flow {
        val poiList = repository.fetchPOIsFromApi(poiId)
        emit(poiList.firstOrNull { it.id.toString() == poiId })
    }
}
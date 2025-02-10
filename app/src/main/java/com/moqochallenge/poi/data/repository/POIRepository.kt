package com.moqochallenge.poi.data.repository

import android.util.Log
import com.moqochallenge.poi.data.local.POIDao
import com.moqochallenge.poi.data.model.POI
import com.moqochallenge.poi.data.remote.POIApi
import javax.inject.Inject

class POIRepository @Inject constructor(
    private val api: POIApi,
    private val poiDao: POIDao
) {
    suspend fun fetchPOIsFromApi(boundingBox: String, pageSize: Int = 10, pageNumber: Int = 1): List<POI> {
        return try {
            val pois = api.getPOIs(boundingBox, pageSize, pageNumber).pois
            //poiDao.insertPOIs(pois)  // Cache API results
            pois
        } catch (e: Exception) {
            //poiDao.getPOIs(-90.0, 90.0, -180.0, 180.0)  // Fetch from Room if API fails
            emptyList()
        }
    }

    suspend fun fetchPOIById(poiId: String): POI? {
        return try {
            val poi = api.getDetailPOI(poiId).pois.firstOrNull()
            //poi?.let { poiDao.insertPOIs(listOf(it)) }  // Cache in Room DB
            poi
        } catch (e: Exception) {
            //poiDao.getPOIById(poiId)  // Fetch from local DB if API
            null
        }
    }
}
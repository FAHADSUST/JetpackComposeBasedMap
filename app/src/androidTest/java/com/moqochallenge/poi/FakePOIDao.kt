package com.moqochallenge.poi

import com.moqochallenge.poi.data.local.POIDao
import com.moqochallenge.poi.data.model.POI
import com.moqochallenge.poi.data.remote.POIApi
import com.moqochallenge.poi.data.remote.POIResponse


class FakePOIDao : POIDao {
    private val localDatabase = mutableListOf<POI>()

    override suspend fun getPOIs(swLat: Double, neLat: Double, swLng: Double, neLng: Double): List<POI> {
        return localDatabase // Returns stored POIs
    }

    override suspend fun getPOIById(poiId: String): POI? {
        return localDatabase.find { it.id == poiId } // Finds POI by ID
    }

    override suspend fun insertPOIs(poi: List<POI>) {
        localDatabase.addAll(poi) // Simulates inserting into Room
    }

    override suspend fun clearPOIs() {
        localDatabase.clear() // Simulates clearing the database
    }
}

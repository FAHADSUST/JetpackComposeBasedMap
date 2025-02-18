package com.moqochallenge.poi

import com.moqochallenge.poi.data.model.POI
import com.moqochallenge.poi.data.remote.POIApi
import com.moqochallenge.poi.data.remote.POIResponse


class FakePOIApi : POIApi {
    private val fakePOIs = mutableListOf<POI>()

    override suspend fun getPOIs(boundingBox: String, pageSize: Int, pageNumber: Int): POIResponse {
        return POIResponse(fakePOIs)
    }

    override suspend fun getDetailPOI(po_id: String, extraFields: String): POIResponse {
        val poi = fakePOIs.find { it.id == po_id }
        return POIResponse(if (poi != null) listOf(poi) else emptyList())
    }

    fun addPOIs(vararg pois: POI) {
        fakePOIs.addAll(pois)
    }
}

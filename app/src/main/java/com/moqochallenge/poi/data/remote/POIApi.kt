package com.moqochallenge.poi.data.remote

import com.google.gson.annotations.SerializedName
import com.moqochallenge.poi.data.model.POI
import retrofit2.http.GET
import retrofit2.http.Query

data class POIResponse(
    @SerializedName("data") val pois: List<POI>
)

interface POIApi {
    @GET("graph/discovery/pois")
    suspend fun getPOIs(
        @Query("filter[bounding_box]") boundingBox: String,
        @Query("page[size]") pageSize: Int = 10,
        @Query("page[number]") pageNumber: Int = 1
    ): POIResponse

    @GET("graph/discovery/pois")
    suspend fun getDetailPOI(
        @Query("filter[id]") po_id: String,
        @Query("extra_fields[pois]") extraFields: String = "image,provider"
    ): POIResponse
}
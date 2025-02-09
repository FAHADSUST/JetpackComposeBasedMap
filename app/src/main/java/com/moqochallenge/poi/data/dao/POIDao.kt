package com.moqochallenge.poi.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.moqochallenge.poi.data.model.POI

@Dao
interface POIDao {

    @Query("SELECT * FROM pois WHERE latitude BETWEEN :swLat AND :neLat AND longitude BETWEEN :swLng AND :neLng")
    suspend fun getPOIs(swLat: Double, neLat: Double, swLng: Double, neLng: Double): List<POI>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPOIs(pois: List<POI>)

    @Query("DELETE FROM pois")
    suspend fun clearPOIs()
}
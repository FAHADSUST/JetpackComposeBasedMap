package com.moqochallenge.poi.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.moqochallenge.poi.data.model.POI

@Dao
interface POIDao {

    @Query("""
        SELECT *
        FROM   poi
        WHERE  latitude BETWEEN :swLat AND :neLat
               AND longitude BETWEEN :swLng AND :neLng
    """)
    suspend fun getPOIs(swLat: Double, neLat: Double, swLng: Double, neLng: Double): List<POI>

    @Query("SELECT * FROM poi WHERE id = :poiId LIMIT 1")
    suspend  fun getPOIById(poiId: String): POI?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPOIs(poi: List<POI>)

    @Query("DELETE FROM poi")
    suspend fun clearPOIs()
}
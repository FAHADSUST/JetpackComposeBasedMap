package com.moqochallenge.poi.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.moqochallenge.poi.data.model.POI

@Database(entities = [POI::class], version = 1, exportSchema = false)
abstract class POIDatabase : RoomDatabase() {
    abstract fun poiDao(): POIDao
}
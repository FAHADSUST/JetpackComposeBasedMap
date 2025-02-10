package com.moqochallenge.poi.data.model
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "poi")
data class POI(
    @PrimaryKey(autoGenerate = true) @SerializedName("id") val id: Int?,
    @SerializedName("lat") val latitude: Double,
    @SerializedName("lng") val longitude: Double,
    @SerializedName("name") val name: String,
    @SerializedName("position_type") val positionType: String?,
    @SerializedName("vehicle_type") val vehicleType: String?,
    @SerializedName("latest_parking_id") val latestParkingId: Long?,
    @SerializedName("app_relation") val appRelation: String?
)

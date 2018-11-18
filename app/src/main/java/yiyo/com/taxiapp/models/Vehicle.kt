package yiyo.com.taxiapp.models

import com.google.android.gms.maps.model.LatLng

data class Vehicle(
    val id: Int,
    val coordinate: LatLng,
    val fleetType: String,
    val heading: Double
)
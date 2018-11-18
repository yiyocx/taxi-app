package yiyo.com.vehiclestest.models

data class Vehicle(
    val id: Int,
    val coordinate: Coordinate,
    val fleetType: String,
    val heading: Double
)
package yiyo.com.taxiapp.api

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import yiyo.com.taxiapp.models.VehiclesResponse

interface MyTaxiApi {

    @GET("/")
    fun getCars(
        @Query(P1_LAT) p1Lat: Double = 53.694865,
        @Query(P1_LON) p1Lon: Double = 9.757589,
        @Query(P2_LAT) p2Lat: Double = 53.394655,
        @Query(P2_LON) p2Lon: Double = 10.099891
    ): Observable<VehiclesResponse>

    companion object {
        const val P1_LAT = "p1Lat"
        const val P1_LON = "p1Lon"
        const val P2_LAT = "p2Lat"
        const val P2_LON = "p2Lon"
    }
}
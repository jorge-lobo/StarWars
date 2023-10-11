package com.example.starwarsjunior.data.ship.remote

import com.example.starwarsjunior.data.ship.objects.ShipListResponse
import retrofit2.http.GET

interface ShipAPI {
    @GET("starships")
    suspend fun getShips(): ShipListResponse
}
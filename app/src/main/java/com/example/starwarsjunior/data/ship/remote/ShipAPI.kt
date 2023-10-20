package com.example.starwarsjunior.data.ship.remote

import com.example.starwarsjunior.data.ship.objects.ShipListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ShipAPI {
    @GET("starships")
    suspend fun getShips(@Query("page") page: Int): ShipListResponse
}
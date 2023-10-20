package com.example.starwarsjunior.data.planet.remote

import com.example.starwarsjunior.data.planet.objects.PlanetListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface PlanetAPI {
    @GET("planets")
    suspend fun getPlanets(@Query("page") page: Int): PlanetListResponse
}
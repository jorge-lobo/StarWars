package com.example.starwarsjunior.data.planet.remote

import com.example.starwarsjunior.data.planet.objects.PlanetListResponse
import retrofit2.http.GET

interface PlanetAPI {
    @GET("planet")
    suspend fun getPlanets(): PlanetListResponse
}
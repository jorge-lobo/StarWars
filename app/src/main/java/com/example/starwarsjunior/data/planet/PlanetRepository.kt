package com.example.starwarsjunior.data.planet

import com.example.starwarsjunior.data.common.ResultWrapper
import com.example.starwarsjunior.data.planet.objects.Planet
import com.example.starwarsjunior.data.planet.objects.PlanetListResponse
import com.example.starwarsjunior.data.planet.remote.PlanetRemoteDataSource
import com.example.starwarsjunior.utils.Utils

object PlanetRepository : IPlanetDataSource.Main {
    private var cachedPlanetResponse = mutableListOf<Planet>()

    override suspend fun getPlanets(page: Int): ResultWrapper<PlanetListResponse> {
        val result = PlanetRemoteDataSource.getPlanets(page)

        result.result?.let {
            //saveDetails(it)
            cachedPlanetResponse.addAll(it.results)
        }
        return result
    }

    suspend fun getCachedPlanets(): List<Planet>? {
        if (cachedPlanetResponse != null) {
            return cachedPlanetResponse
        }
        return null
    }

    override suspend fun getCachedPlanetName(planetID: Int): Planet? {
        for (item in cachedPlanetResponse.orEmpty()) {
            //Extract ID number from URL
            if (Utils.extractIdFromUrl(item.url) == planetID) {
                return item
            }
        }
        return null
    }

    override suspend fun getCachedPlanet(planetID: Int): ResultWrapper<Planet?> {
        for (item in cachedPlanetResponse.orEmpty()) {
            //Extract ID number from URL
            if (Utils.extractIdFromUrl(item.url) == planetID) {
                return ResultWrapper(item, null)
            }
        }
        return ResultWrapper(null, null)
    }
}
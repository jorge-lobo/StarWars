package com.example.starwarsjunior.data.planet

import com.example.starwarsjunior.data.common.ResultWrapper
import com.example.starwarsjunior.data.planet.objects.Planet
import com.example.starwarsjunior.data.planet.objects.PlanetListResponse
import com.example.starwarsjunior.data.planet.remote.PlanetRemoteDataSource
import com.example.starwarsjunior.utils.Utils

object PlanetRepository : IPlanetDataSource.Main {
    private var cachedPlanetResponse: List<Planet>? = null

    override suspend fun getPlanets(): ResultWrapper<PlanetListResponse> {
        //check if the data is already cached
        cachedPlanetResponse?.let {
            return ResultWrapper(PlanetListResponse(it), null)
        }
        //if not cached, make a call to API
        val result = PlanetRemoteDataSource.getPlanets()

        result.result?.let {
            //saveDetails(it)
            cachedPlanetResponse = it.results
        }
        return result
    }

    suspend fun getCachedPlanets(): List<Planet>? {
        if (cachedPlanetResponse != null) {
            return cachedPlanetResponse
        }
        return null
    }

    override suspend fun getCachedPlanet(planetID: Int): Planet? {
        for (item in cachedPlanetResponse.orEmpty()) {
            //Extract ID number from URL
            if (Utils.extractIdFromUrl(item.url) == planetID) {
                return item
            }
        }
        return null
    }
}
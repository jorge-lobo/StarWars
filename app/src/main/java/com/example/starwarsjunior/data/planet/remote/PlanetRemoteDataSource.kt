package com.example.starwarsjunior.data.planet.remote

import com.example.starwarsjunior.data.common.HTTPClient
import com.example.starwarsjunior.data.common.ResultWrapper
import com.example.starwarsjunior.data.planet.IPlanetDataSource
import com.example.starwarsjunior.data.planet.objects.PlanetListResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

object PlanetRemoteDataSource: IPlanetDataSource.Remote {

    private val planetAPI = HTTPClient(PlanetAPI::class.java).get()
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO

    override suspend fun getPlanets(): ResultWrapper<PlanetListResponse> {
        return ResultWrapper.safeApiCall(dispatcher) {
            planetAPI.getPlanets()
        }
    }
}
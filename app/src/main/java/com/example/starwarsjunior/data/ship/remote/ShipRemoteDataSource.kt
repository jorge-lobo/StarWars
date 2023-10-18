package com.example.starwarsjunior.data.ship.remote

import com.example.starwarsjunior.data.common.HTTPClient
import com.example.starwarsjunior.data.common.ResultWrapper
import com.example.starwarsjunior.data.ship.IShipDataSource
import com.example.starwarsjunior.data.ship.objects.ShipListResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

object ShipRemoteDataSource : IShipDataSource.Remote {

    private val shipAPI = HTTPClient(ShipAPI::class.java).get()
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO

    override suspend fun getShips(pagination: Int): ResultWrapper<ShipListResponse> {
        return ResultWrapper.safeApiCall(dispatcher) {
            shipAPI.getShips(pagination)
        }
    }
}
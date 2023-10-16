package com.example.starwarsjunior.data.ship

import com.example.starwarsjunior.data.common.ResultWrapper
import com.example.starwarsjunior.data.ship.objects.Ship
import com.example.starwarsjunior.data.ship.objects.ShipListResponse
import com.example.starwarsjunior.data.ship.remote.ShipRemoteDataSource
import com.example.starwarsjunior.utils.Utils

object ShipRepository : IShipDataSource.Main {
    private var cachedShipResponse: List<Ship>? = null

    override suspend fun getShips(): ResultWrapper<ShipListResponse> {
        //check if the data is already cached
        cachedShipResponse?.let {
            return ResultWrapper(ShipListResponse(it), null)
        }
        //if not cached, make a call to API
        val result = ShipRemoteDataSource.getShips()

        result.result?.let {
            //saveDetails(it)
            cachedShipResponse = it.results
        }
        return result
    }

    suspend fun getCachedShips(): List<Ship>? {
        if (cachedShipResponse != null) {
            return cachedShipResponse
        }
        return null
    }

    override suspend fun getCachedShip(shipID: Int): ResultWrapper<Ship?> {
        for (item in cachedShipResponse.orEmpty()) {
            //Extract ID number from URL
            if (Utils.extractIdFromUrl(item.url) == shipID) {
                return ResultWrapper(item, null)
            }
        }
        return ResultWrapper(null, null)
    }
}
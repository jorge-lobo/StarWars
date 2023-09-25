package com.example.starwarsjunior.data.personage

import com.example.starwarsjunior.data.common.ResultWrapper
import com.example.starwarsjunior.data.personage.objects.Personage
import com.example.starwarsjunior.data.personage.objects.PersonageListResponse
import com.example.starwarsjunior.data.personage.remote.PersonageRemoteDataSource
import com.example.starwarsjunior.utils.Utils.extractIdFromUrl

object PersonageRepository : IPersonageDataSource.Main {
    private var cachedPersonageResponse: List<Personage>? = null

    override suspend fun getPersonages(): ResultWrapper<PersonageListResponse> {
        val result = PersonageRemoteDataSource.getPersonages()

        result.result?.let {
            //saveDetails(it)
            cachedPersonageResponse = it.results
        }

        return result
    }

    override suspend fun getCachedPersonage(personageID: Int): ResultWrapper<Personage?> {
        for (item in cachedPersonageResponse!!) {
            //Extract ID number from URL
            val idFromUrl = extractIdFromUrl(item.url)

            if (idFromUrl == personageID) {
                return ResultWrapper(item, null)
            }
        }

        return ResultWrapper(null, null)
    }


}
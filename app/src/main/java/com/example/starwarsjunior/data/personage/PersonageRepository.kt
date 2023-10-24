package com.example.starwarsjunior.data.personage

import com.example.starwarsjunior.data.common.ResultWrapper
import com.example.starwarsjunior.data.personage.objects.Personage
import com.example.starwarsjunior.data.personage.objects.PersonageListResponse
import com.example.starwarsjunior.data.personage.objects.Specie
import com.example.starwarsjunior.data.personage.objects.SpecieListResponse
import com.example.starwarsjunior.data.personage.remote.PersonageRemoteDataSource
import com.example.starwarsjunior.utils.Utils

object PersonageRepository : IPersonageDataSource.Main {
    private var cachedPersonageResponse = mutableListOf<Personage>()
    private var cachedSpecieResponse = mutableListOf<Specie>()

    override suspend fun getPersonages(page: Int): ResultWrapper<PersonageListResponse> {
        val result = PersonageRemoteDataSource.getPersonages(page)

        result.result?.let {
            //saveDetails(it)
            cachedPersonageResponse.addAll(it.results)
        }
        return result
    }

    suspend fun getCachedPersonages(): List<Personage>? {
        if (cachedPersonageResponse != null) {
            return cachedPersonageResponse
        }
        return null
    }

    override suspend fun getSpecies(page: Int): ResultWrapper<SpecieListResponse> {
        val result = PersonageRemoteDataSource.getSpecies(page)

        result.result?.let {
            //saveDetails(it)
            cachedSpecieResponse.addAll(it.results)
        }
        return result
    }

    suspend fun getCachedSpecies(): List<Specie>? {
        if (cachedSpecieResponse != null) {
            return cachedSpecieResponse
        }
        return null
    }

    override suspend fun getCachedPersonage(personageID: Int): ResultWrapper<Personage?> {
        for (item in cachedPersonageResponse.orEmpty()) {
            //Extract ID number from URL
            if (Utils.extractIdFromUrl(item.url) == personageID) {
                return ResultWrapper(item, null)
            }
        }
        return ResultWrapper(null, null)
    }

    override suspend fun getCachedSpecie(specieID: Int): Specie? {
        for (item in cachedSpecieResponse.orEmpty()) {
            //Extract ID number from URL
            if (Utils.extractIdFromUrl(item.url) == specieID) {
                return item; null
            }
        }
        return null; null
    }
}
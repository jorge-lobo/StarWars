package com.example.starwarsjunior.data.personage

import com.example.starwarsjunior.data.common.ResultWrapper
import com.example.starwarsjunior.data.personage.objects.Personage
import com.example.starwarsjunior.data.personage.objects.PersonageListResponse
import com.example.starwarsjunior.data.personage.objects.Specie
import com.example.starwarsjunior.data.personage.objects.SpecieListResponse
import com.example.starwarsjunior.data.personage.remote.PersonageRemoteDataSource
import com.example.starwarsjunior.utils.Utils
import com.example.starwarsjunior.utils.Utils.extractIdFromUrl
import org.json.JSONException
import org.json.JSONObject

object PersonageRepository : IPersonageDataSource.Main {
    private var cachedPersonageResponse: List<Personage>? = null
    private var cachedSpecieResponse: List<Specie>? = null

    override suspend fun getPersonages(): ResultWrapper<PersonageListResponse> {
        //check if the data is already cached
        cachedPersonageResponse?.let {
            return ResultWrapper(PersonageListResponse(it), null)
        }

        //if not cached, make a call to API
        val result = PersonageRemoteDataSource.getPersonages()

        result.result?.let {
            //saveDetails(it)
            cachedPersonageResponse = it.results
        }
        return result
    }

    override suspend fun getSpecies(): ResultWrapper<SpecieListResponse> {
        //check if the data is already cached
        cachedSpecieResponse?.let {
            return ResultWrapper(SpecieListResponse(it), null)
        }

        //if not cached, make a call to API
        val result = PersonageRemoteDataSource.getSpecies()

        result.result?.let {
            //saveDetails(it)
            cachedSpecieResponse = it.results
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

    override suspend fun getCachedSpecie(specieID: Int): Specie? {
        for (item in cachedSpecieResponse.orEmpty()) {
            //Extract ID number from URL
            if (Utils.extractIdFromUrl(item.url) == specieID) {
                return item; null
            }
        }
        return null; null
    }

    /*suspend fun fetchSpeciesName(speciesUrl: String): String? {
        return withContext(Dispatchers.IO) {
            val request = Request.Builder()
                .url(speciesUrl)
                .build()

            val client = OkHttpClient()
            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                val responseBody = response.body?.string()
                return@withContext parseSpeciesNameFromJson(responseBody)
            }
            return@withContext null
        }
    }*/

    private fun parseSpeciesNameFromJson(json: String?): String? {
        try {
            val jsonObject = JSONObject(json)
            return jsonObject.optString("name", null)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return null
    }
}
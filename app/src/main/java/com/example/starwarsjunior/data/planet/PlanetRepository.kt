package com.example.starwarsjunior.data.planet

import com.example.starwarsjunior.data.common.ResultWrapper
import com.example.starwarsjunior.data.planet.objects.Planet
import com.example.starwarsjunior.data.planet.objects.PlanetListResponse
import com.example.starwarsjunior.data.planet.remote.PlanetRemoteDataSource
import com.example.starwarsjunior.utils.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONException
import org.json.JSONObject

object PlanetRepository : IPlanetDataSource.Main {
    private var cachedPlanetResponse: List<Planet>? = null

    override suspend fun getPlanets(): ResultWrapper<PlanetListResponse> {
        val result = PlanetRemoteDataSource.getPlanets()

        result.result?.let {
            //saveDetails(it)
            cachedPlanetResponse = it.results
        }

        return result
    }

    override suspend fun getCachedPlanet(planetID: Int): ResultWrapper<Planet?> {
        for (item in cachedPlanetResponse!!) {
            //Extract ID number from URL
            val idFromUrl = Utils.extractIdFromUrl(item.url)

            if (idFromUrl == planetID) {
                return ResultWrapper(item, null)
            }
        }

        return ResultWrapper(null, null)
    }

    fun extractPlanetID(planetUrl: String): Int? {
        // Split the URL by '/' and get the last part, which should be the planet ID
        val parts = planetUrl.split("/")
        if (parts.isNotEmpty()) {
            val planetID = parts.last()
            return planetID.toIntOrNull()
        }
        return null
    }

    suspend fun fetchPlanetName(planetUrl: String): String? {
        return withContext(Dispatchers.IO) {
            val request = Request.Builder()
                .url(planetUrl)
                .build()

            val client = OkHttpClient()
            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                val responseBody = response.body?.string()
                return@withContext parsePlanetNameFromJson(responseBody)
            }
            return@withContext null
        }
    }

    private fun parsePlanetNameFromJson(json: String?): String? {
        try {
            val jsonObject = JSONObject(json)
            return jsonObject.optString("name", null)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return null
    }
}
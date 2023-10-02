package com.example.starwarsjunior.data.personage.remote

import com.example.starwarsjunior.data.personage.objects.PersonageListResponse
import com.example.starwarsjunior.data.personage.objects.Specie
import retrofit2.http.GET
import retrofit2.http.Url

interface PersonageAPI {
    @GET("people")
    suspend fun getPersonages(): PersonageListResponse

    @GET("species")
    suspend fun getSpecie(@Url specieName: String): Specie
}
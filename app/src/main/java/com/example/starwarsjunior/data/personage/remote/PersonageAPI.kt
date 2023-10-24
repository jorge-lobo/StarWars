package com.example.starwarsjunior.data.personage.remote

import com.example.starwarsjunior.data.personage.objects.PersonageListResponse
import com.example.starwarsjunior.data.personage.objects.SpecieListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface PersonageAPI {
    @GET("people")
    suspend fun getPersonages(@Query("page") page: Int): PersonageListResponse

    @GET("species")
    suspend fun getSpecies(@Query("page") page: Int): SpecieListResponse
}
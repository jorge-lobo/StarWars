package com.example.starwarsjunior.data.personage.remote

import com.example.starwarsjunior.data.personage.objects.PersonageListResponse
import retrofit2.http.GET

interface PersonageAPI {
    @GET("people")
    suspend fun getPersonages(): PersonageListResponse
}
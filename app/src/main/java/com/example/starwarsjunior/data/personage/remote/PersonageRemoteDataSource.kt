package com.example.starwarsjunior.data.personage.remote

import com.example.starwarsjunior.data.common.HTTPClient
import com.example.starwarsjunior.data.common.ResultWrapper
import com.example.starwarsjunior.data.personage.IPersonageDataSource
import com.example.starwarsjunior.data.personage.objects.PersonageListResponse
import com.example.starwarsjunior.data.personage.objects.SpecieListResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

object PersonageRemoteDataSource: IPersonageDataSource.Remote {

    private val personageAPI = HTTPClient(PersonageAPI::class.java).get()
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO

    override suspend fun getPersonages(page: Int): ResultWrapper<PersonageListResponse> {
        return ResultWrapper.safeApiCall(dispatcher) {
            personageAPI.getPersonages(page)
        }
    }

    override suspend fun getSpecies(page: Int): ResultWrapper<SpecieListResponse> {
        return ResultWrapper.safeApiCall(dispatcher) {
            personageAPI.getSpecies(page)
        }
    }
}
package com.example.starwarsjunior.ui

import android.app.Application
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.starwarsjunior.data.error.CallbackWrapper
import com.example.starwarsjunior.data.personage.PersonageRepository
import com.example.starwarsjunior.data.personage.objects.Personage
import com.example.starwarsjunior.data.personage.objects.PersonageListResponse
import com.example.starwarsjunior.data.personage.objects.Specie
import com.example.starwarsjunior.data.personage.objects.SpecieListResponse
import com.example.starwarsjunior.data.planet.PlanetRepository
import com.example.starwarsjunior.data.planet.objects.Planet
import com.example.starwarsjunior.data.planet.objects.PlanetListResponse
import com.example.starwarsjunior.data.ship.ShipRepository
import com.example.starwarsjunior.data.ship.objects.Ship
import com.example.starwarsjunior.data.ship.objects.ShipListResponse
import com.example.starwarsjunior.ui.common.BaseViewModel
import kotlinx.coroutines.launch

class SplashScreenViewModel(application: Application) : BaseViewModel(application),
    LifecycleObserver {

    private var personages = MutableLiveData<List<Personage>>()
    private var planets = MutableLiveData<List<Planet>>()
    private var species = MutableLiveData<List<Specie>>()
    private var ships = MutableLiveData<List<Ship>>()
    val preloadComplete = MutableLiveData<Int>().apply { value = 0 }

    fun preloadDataFromAPI() {

        isLoading.value = true

        noDataAvailable.value = false
        personages.value = emptyList()
        planets.value = emptyList()
        species.value = emptyList()
        ships.value = emptyList()

        viewModelScope.launch {
            val personagesResponse = PersonageRepository.getPersonages()
            val planetsResponse = PlanetRepository.getPlanets()
            val speciesResponse = PersonageRepository.getSpecies()

            val shipsResponse = ShipRepository.getShips(1)
            val totalCount = shipsResponse.result?.count

            val itemsPerPage = 10
            val totalPages = totalCount?.let { calculateTotalPages(it, itemsPerPage) }

            for (page in 1..totalPages!!) {
                ShipRepository.getShips(page)
            }

            object : CallbackWrapper<PersonageListResponse>(
                this@SplashScreenViewModel,
                personagesResponse
            ) {
                override fun onSuccess(data: PersonageListResponse) {
                    preloadComplete.value = preloadComplete.value!! + 1
                }
            }

            object : CallbackWrapper<PlanetListResponse>(
                this@SplashScreenViewModel,
                planetsResponse
            ) {
                override fun onSuccess(data: PlanetListResponse) {
                    preloadComplete.value = preloadComplete.value!! + 1
                }
            }

            object : CallbackWrapper<SpecieListResponse>(
                this@SplashScreenViewModel,
                speciesResponse
            ) {
                override fun onSuccess(data: SpecieListResponse) {
                    preloadComplete.value = preloadComplete.value!! + 1
                }
            }

            object : CallbackWrapper<ShipListResponse>(
                this@SplashScreenViewModel,
                shipsResponse
            ) {
                override fun onSuccess(data: ShipListResponse) {
                    preloadComplete.value = preloadComplete.value!! + 1
                }
            }
        }
    }

    private fun calculateTotalPages(count: Int, itemsPerPage: Int): Int {
        return (count + itemsPerPage - 1) / itemsPerPage
    }

    override fun onError(message: String?, validationErrors: Map<String, ArrayList<String>>?) {
        isLoading.value = false
        isRefreshing.value = false
    }
}
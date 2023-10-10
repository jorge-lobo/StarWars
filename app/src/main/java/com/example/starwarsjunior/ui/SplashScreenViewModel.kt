package com.example.starwarsjunior.ui

import android.app.Application
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.starwarsjunior.data.error.CallbackWrapper
import com.example.starwarsjunior.data.personage.PersonageRepository
import com.example.starwarsjunior.data.personage.objects.Personage
import com.example.starwarsjunior.data.personage.objects.PersonageListResponse
import com.example.starwarsjunior.data.planet.PlanetRepository
import com.example.starwarsjunior.data.planet.objects.Planet
import com.example.starwarsjunior.data.planet.objects.PlanetListResponse
import com.example.starwarsjunior.ui.common.BaseViewModel
import kotlinx.coroutines.launch

class SplashScreenViewModel(application: Application) : BaseViewModel(application),
    LifecycleObserver {

    private var personages = MutableLiveData<List<Personage>>()
    private var planets = MutableLiveData<List<Planet>>()
    val preloadComplete = MutableLiveData<Int>().apply { value = 0 }

    fun preloadDataFromAPI() {

        isLoading.value = true

        noDataAvailable.value = false
        personages.value = emptyList()
        planets.value = emptyList()

        viewModelScope.launch {
            val personagesResponse = PersonageRepository.getPersonages()
            val planetsResponse = PlanetRepository.getPlanets()

            var result = object : CallbackWrapper<PersonageListResponse>(
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
        }
    }

    override fun onError(message: String?, validationErrors: Map<String, ArrayList<String>>?) {
        isLoading.value = false
        isRefreshing.value = false
    }
}
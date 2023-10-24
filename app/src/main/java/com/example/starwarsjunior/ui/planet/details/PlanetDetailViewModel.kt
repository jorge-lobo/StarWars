package com.example.starwarsjunior.ui.planet.details

import android.app.Application
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.starwarsjunior.data.error.CallbackWrapper
import com.example.starwarsjunior.data.planet.PlanetRepository
import com.example.starwarsjunior.data.planet.objects.Planet
import com.example.starwarsjunior.ui.common.BaseViewModel
import kotlinx.coroutines.launch
import java.text.DecimalFormat

class PlanetDetailViewModel(application: Application) : BaseViewModel(application),
    LifecycleObserver {

    var planetName = MutableLiveData<String>().apply { value = "" }
    var planetRotationPeriod = MutableLiveData<String>().apply { value = "" }
    var planetOrbitalPeriod = MutableLiveData<String>().apply { value = "" }
    var planetDiameter = MutableLiveData<String>().apply { value = "" }
    var planetGravity = MutableLiveData<String>().apply { value = "" }
    var planetClimate = MutableLiveData<String>().apply { value = "" }
    var planetTerrain = MutableLiveData<String>().apply { value = "" }
    var planetSurfaceWater = MutableLiveData<String>().apply { value = "" }
    var planetPopulation = MutableLiveData<String>().apply { value = "" }

    fun initialize(planetId: Int) {
        getPlanetDetails(planetId)
    }

    private fun getPlanetDetails(planetId: Int) {
        isLoading.value = true

        viewModelScope.launch {
            val planetResponse = PlanetRepository.getCachedPlanet(planetId)
            val result =
                object :
                    CallbackWrapper<Planet?>(this@PlanetDetailViewModel, planetResponse) {
                    override fun onSuccess(data: Planet?) {
                        if (data != null) {
                            planetName.value = data.name.lowercase()
                            planetRotationPeriod.value = data.rotationPeriod.lowercase()
                            planetOrbitalPeriod.value = formatter(data.orbitalPeriod.lowercase())
                            planetDiameter.value = formatter(data.diameter.lowercase())
                            planetGravity.value = getList(data.gravity.lowercase())
                            planetClimate.value = getList(data.climate.lowercase())
                            planetTerrain.value = getList(data.terrain.lowercase())
                            planetSurfaceWater.value = data.surfaceWater.lowercase()
                            planetPopulation.value = formatter(data.population.lowercase())

                        } else {
                            onError("Unknown error")
                        }
                        isLoading.value = false
                    }
                }
        }
    }

    fun formatter(n: String): String? = runCatching {
        val numberSeparator = n.replace(",", "").toInt()
        DecimalFormat("#,###").format(numberSeparator)
    }.getOrElse { n }

    fun getList(n: String): String {
        val value = n.replace(",\\s+".toRegex(), ",")
        return value.split(",").joinToString("\n")
    }

    override fun onError(message: String?, validationErrors: Map<String, ArrayList<String>>?) {
        isLoading.value = false
        isRefreshing.value = false
    }
}
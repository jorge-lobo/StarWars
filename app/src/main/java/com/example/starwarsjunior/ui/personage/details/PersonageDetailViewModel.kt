package com.example.starwarsjunior.ui.personage.details

import android.app.Application
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.starwarsjunior.data.error.CallbackWrapper
import com.example.starwarsjunior.data.personage.PersonageRepository
import com.example.starwarsjunior.data.personage.PersonageRepository.fetchSpeciesName
import com.example.starwarsjunior.data.personage.objects.Personage
import com.example.starwarsjunior.data.planet.PlanetRepository
import com.example.starwarsjunior.ui.common.BaseViewModel
import kotlinx.coroutines.launch

class PersonageDetailViewModel(application: Application) : BaseViewModel(application),
    LifecycleObserver {

    var personageName = MutableLiveData<String>().apply { value = "" }
    var personageBirthYear = MutableLiveData<String>().apply { value = "" }
    var personageGender = MutableLiveData<String>().apply { value = "" }
    var personageHeight = MutableLiveData<String>().apply { value = "" }
    var personageMass = MutableLiveData<String>().apply { value = "" }
    var personageHairColor = MutableLiveData<String>().apply { value = "" }
    var personageSkinColor = MutableLiveData<String>().apply { value = "" }
    var personageEyeColor = MutableLiveData<String>().apply { value = "" }
    var personageHomeWorld = MutableLiveData<String>().apply { value = "" }
    var personageSpecie = MutableLiveData<String>().apply { value = "" }

    fun initialize(personageId: Int) {
        getPersonageDetails(personageId)
    }

    private fun getPersonageDetails(personageId: Int) {
        isLoading.value = true

        viewModelScope.launch {
            val personageResponse = PersonageRepository.getCachedPersonage(personageId)
            val result =
                object :
                    CallbackWrapper<Personage?>(this@PersonageDetailViewModel, personageResponse) {
                    override fun onSuccess(data: Personage?) {
                        if (data != null) {
                            personageName.value = data.name.lowercase()
                            personageBirthYear.value = data.birthYear.lowercase()
                            personageGender.value = data.gender.lowercase()
                            personageHeight.value = data.height.toString()
                            personageMass.value = data.mass.toString()
                            personageHairColor.value = data.hairColor.lowercase()
                            personageSkinColor.value = data.skinColor.lowercase()
                            personageEyeColor.value = data.eyeColor.lowercase()

                            getHomeWorldName(data.homeWorld)
                            getSpeciesNames(data.species)

                            isLoading.value = false
                        } else {
                            onError("Unknown error")
                        }
                    }
                }
        }
    }

    private fun getHomeWorldName(planetUrl: String) {
        viewModelScope.launch {
            val planetID = extractPlanetID(planetUrl)

            planetID?.let {
                val planetObject = PlanetRepository.getCachedPlanet(it)
                personageHomeWorld.value = planetObject?.name
            }
        }
    }

    private fun extractPlanetID(planetUrl: String): Int? {
        // Split the URL by '/' and get the second part from the end, which should be the planet ID
        val parts = planetUrl.split("/")
        if (parts.isNotEmpty()) {
            val planetID = parts[parts.size - 2]
            return planetID.toIntOrNull()
        }
        return null
    }

    private fun getSpeciesNames(speciesUrls: List<String>) {
        viewModelScope.launch {
            val speciesNamesList = mutableListOf<String>()
            for (speciesUrl in speciesUrls) {
                val speciesName = fetchSpeciesName(speciesUrl)
                speciesName?.let {
                    speciesNamesList.add(it)
                }
            }

            if (speciesNamesList.isNotEmpty()) {
                val combinedSpeciesNames = speciesNamesList.joinToString(", ").lowercase()
                personageSpecie.postValue(combinedSpeciesNames)
            } else {
                personageSpecie.value = "human"
            }
        }
    }

    override fun onError(message: String?, validationErrors: Map<String, ArrayList<String>>?) {
        isLoading.value = false
        isRefreshing.value = false
    }
}
package com.example.starwarsjunior.ui.personage.details

import android.app.Application
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.*
import com.example.starwarsjunior.data.error.CallbackWrapper
import com.example.starwarsjunior.data.personage.PersonageRepository
import com.example.starwarsjunior.data.personage.objects.Personage
import com.example.starwarsjunior.ui.common.BaseViewModel
import kotlinx.coroutines.launch

class PersonageDetailViewModel(application: Application) : BaseViewModel(application),
    LifecycleObserver {

    //Mutables change things directly in the View Layer, so in fragments and activities
    var personageName = MutableLiveData<String>().apply { value = "" }
    var personageBirthYear = MutableLiveData<String>().apply { value = "" }
    var personageSpecies = MutableLiveData<String>().apply { value = "" }
    var personageGender = MutableLiveData<String>().apply { value = "" }
    var personageHeight = MutableLiveData<Int>().apply { value = null }
    var personageMass = MutableLiveData<Int>().apply { value = null }
    var personageHairColor = MutableLiveData<String>().apply { value = "" }
    var personageSkinColor = MutableLiveData<String>().apply { value = "" }
    var personageEyeColor = MutableLiveData<String>().apply { value = "" }
    var personageHomeWorld = MutableLiveData<String>().apply { value = "" }

    fun initialize(personageId: Int) {
        getPersonageDetails(personageId)
    }

    private fun getPersonageDetails(personageId: Int) {

        isLoading.value = true

        viewModelScope.launch {
            val personageResponse = PersonageRepository.getCachedPersonage(personageId)
            var result =
                object :
                    CallbackWrapper<Personage?>(this@PersonageDetailViewModel, personageResponse) {
                    override fun onSuccess(data: Personage?) {
                        if (data != null) {

                            var specieList = ""
                            var homeWorldList = ""

                            personageName.value = data.name.lowercase()
                            personageBirthYear.value = data.birthYear.lowercase()
                            personageGender.value = data.gender.lowercase()
                            personageHeight.value = data.height
                            personageMass.value = data.mass
                            personageHairColor.value = data.hairColor.lowercase()
                            personageSkinColor.value = data.skinColor.lowercase()
                            personageEyeColor.value = data.eyeColor.lowercase()

                            if (!data.species.isEmpty()) {
                                for (specie in data.species) {
                                    specieList += specie.name + ";"
                                }
                                personageSpecies.value = specieList
                            } else {
                                personageSpecies.value = "Data not available"
                            }
                            isLoading.value = false

                            if (!data.homeWorld.isEmpty()) {
                                for (homeWorld in data.homeWorld) {
                                    homeWorldList += homeWorld.name + ";"
                                }
                                personageHomeWorld.value = homeWorldList
                            } else {
                                personageHomeWorld.value = "Data not available"
                            }
                            isLoading.value = false
                        }
                    }
                }
        }
    }

    override fun onError(message: String?, validationErrors: Map<String, ArrayList<String>>?) {
        isLoading.value = false
        isRefreshing.value = false
    }
}
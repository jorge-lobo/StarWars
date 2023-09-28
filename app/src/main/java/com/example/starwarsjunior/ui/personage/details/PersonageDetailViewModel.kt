package com.example.starwarsjunior.ui.personage.details

import android.app.Application
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.starwarsjunior.data.error.CallbackWrapper
import com.example.starwarsjunior.data.personage.PersonageRepository
import com.example.starwarsjunior.data.personage.objects.Personage
import com.example.starwarsjunior.ui.common.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONException
import org.json.JSONObject

class PersonageDetailViewModel(application: Application) : BaseViewModel(application),
    LifecycleObserver {

    private val client = OkHttpClient()

    //Mutables change things directly in the View Layer, so in fragments and activities
    var personageName = MutableLiveData<String>().apply { value = "" }
    var personageBirthYear = MutableLiveData<String>().apply { value = "" }
    var personageSpecie = MutableLiveData<String>().apply { value = "" }
    var personageGender = MutableLiveData<String>().apply { value = "" }
    var personageHeight = MutableLiveData<String>().apply { value = "" }
    var personageMass = MutableLiveData<String>().apply { value = "" }
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
                            personageName.value = data.name.lowercase()
                            personageBirthYear.value = data.birthYear.lowercase()
                            personageGender.value = data.gender.lowercase()
                            personageHeight.value = data.height.toString()
                            personageMass.value = data.mass.toString()
                            personageHairColor.value = data.hairColor.lowercase()
                            personageSkinColor.value = data.skinColor.lowercase()
                            personageEyeColor.value = data.eyeColor.lowercase()

                            getHomeWorldName(data.homeWorld)

                        }
                    }
                }
            isLoading.value = false
        }
    }

    override fun onError(message: String?, validationErrors: Map<String, ArrayList<String>>?) {
        isLoading.value = false
        isRefreshing.value = false
    }

    fun getHomeWorldName(planetUrl: String) {
        viewModelScope.launch {
            val planetName = fetchPlanetName(planetUrl)
            personageHomeWorld.value = planetName ?: "N/A"
        }
    }

    private suspend fun fetchPlanetName(planetUrl: String): String? {
        return withContext(Dispatchers.IO) {
            val request = Request.Builder()
                .url(planetUrl)
                .build()

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
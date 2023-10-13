package com.example.starwarsjunior.ui.planet

import android.app.Application
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.starwarsjunior.data.error.CallbackWrapper
import com.example.starwarsjunior.data.planet.PlanetRepository
import com.example.starwarsjunior.data.planet.objects.Planet
import com.example.starwarsjunior.data.planet.objects.PlanetListResponse
import com.example.starwarsjunior.ui.common.BaseViewModel
import kotlinx.coroutines.launch

class PlanetViewModel(application: Application) : BaseViewModel(application),
    LifecycleObserver {

    var planets = MutableLiveData<List<Planet>>()
    val filteredPlanets = MutableLiveData<List<Planet>?>()
    val searchQuery = MutableLiveData<String>()
    private var isDataPreloaded = false

    val sortedPlanets = MutableLiveData<List<Planet>?>()
    private var sortBy = "name"
    private var isDescending = false

    private val selectedFilters = mutableSetOf<String>()

    init {
        observerSearchQuery()
    }

    fun isDataPreloaded(): Boolean {
        return isDataPreloaded
    }

    fun setPreloadedDataFlag(preloaded: Boolean) {
        isDataPreloaded = preloaded
    }

    fun onRefresh() {
        //isRefreshing.value = true
        getPlanets(refresh = false)
    }

    fun onStart() {
        //getPlanets(forceRefresh)
        getPlanets(true)
    }

    fun getPlanets(refresh: Boolean) {
        if (refresh) {
            isLoading.value = true
        }

        noDataAvailable.value = false
        planets.value = emptyList()

        viewModelScope.launch {
            val planetsResponse = PlanetRepository.getPlanets()
            var result = object : CallbackWrapper<PlanetListResponse>(
                this@PlanetViewModel,
                planetsResponse
            ) {
                override fun onSuccess(data: PlanetListResponse) {
                    //sort list by planet's name
                    planets.value = data.results.sortedBy { it.name }
                    sortedPlanets.value = planets.value
                    isLoading.value = false
                    isRefreshing.value = false
                    data.let {
                        if (it.results.isEmpty()) {
                            noDataAvailable.value = true
                        }
                    }
                }
            }
        }
    }

    //SearchBox
    private fun observerSearchQuery() {
        searchQuery.observeForever { query ->
            val filteredList = planets.value?.filter { planet ->
                planet.name.contains(query, ignoreCase = true)
            }
            filteredPlanets.value = filteredList
        }
    }

    override fun onError(message: String?, validationErrors: Map<String, ArrayList<String>>?) {
        isLoading.value = false
        isRefreshing.value = false
        planets.value = arrayListOf()
    }

    //Order Buttons
    fun toggleSortNameOrder() {
        sortBy = "name"
        isDescending = !isDescending
        updateSortedPlanets()
    }

    fun toggleSortedSizeOrder() {
        sortBy = "diameter"
        isDescending = !isDescending
        updateSortedPlanets()
    }

    private fun updateSortedPlanets() {
        val sortedList = when (sortBy) {
            "name" -> {
                if (isDescending) sortedPlanets.value?.sortedByDescending { it.name }
                else sortedPlanets.value?.sortedBy { it.name }
            }

            "diameter" -> {
                if (isDescending) sortedPlanets.value?.sortedByDescending { it.diameter }
                else sortedPlanets.value?.sortedBy { it.diameter }
            }

            else -> sortedPlanets.value
        }
        sortedPlanets.value = sortedList
    }

    //Filter Buttons

    //add or remove a selected filter
    fun toggleFilter(filter: String) {
        if (selectedFilters.contains(filter)) {
            selectedFilters.remove(filter)
        } else {
            selectedFilters.add(filter)
        }
    }

    fun applyFilters() {
        // TODO()
        /*val filteredList = planets.value?.filter { planet ->
            selectedFilters.isEmpty() || selectedFilters.contains(planet.TODO)
        }
        filteredPlanets.value = filteredList*/
    }

    fun isFilterSelected(filter: String): Boolean {
        return selectedFilters.contains(filter)
    }

}
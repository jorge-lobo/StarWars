package com.example.starwarsjunior.ui.planet

import android.app.Application
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.starwarsjunior.data.planet.PlanetRepository
import com.example.starwarsjunior.data.planet.objects.Planet
import com.example.starwarsjunior.ui.common.BaseViewModel
import kotlinx.coroutines.launch

class PlanetViewModel(application: Application) : BaseViewModel(application),
    LifecycleObserver {

    private var planets = MutableLiveData<List<Planet>?>()
    val filteredPlanets = MutableLiveData<List<Planet>?>()
    val sortedPlanets = MutableLiveData<List<Planet>?>()

    val searchQuery = MutableLiveData<String>()
    val resultsNotFoundMessage = MutableLiveData<Boolean>(false)

    private var isDataPreloaded = false

    enum class SortBy {
        NAME, DIAMETER
    }

    private var sortBy = SortBy.NAME
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

    fun onStart() {
        //getPlanets(forceRefresh)
        getCachedPlanets(true)
    }

    fun getCachedPlanets(refresh: Boolean) {
        if (refresh) {
            isLoading.value = true
        }

        noDataAvailable.value = false
        planets.value = emptyList()

        viewModelScope.launch {
            val result = PlanetRepository.getCachedPlanets()
            //sort list by planet's name
            planets.value = result?.sortedBy { it.name }
            sortedPlanets.value = planets.value
            isLoading.value = false
            isRefreshing.value = false
            result?.let {
                if (it.isEmpty()) {
                    noDataAvailable.value = true
                }
            }
        }
    }

    //SearchBox
    private fun observerSearchQuery() {
        searchQuery.observeForever { query ->
            if (selectedFilters.isEmpty()) {
                val filteredList = planets.value?.filter { planet ->
                    planet.name.contains(query, ignoreCase = true)
                }
                filteredPlanets.value = filteredList
            } else {
                val filteredList = filteredPlanets.value?.filter { planet ->
                    planet.name.contains(query, ignoreCase = true)
                }
                filteredPlanets.value = filteredList
            }

            //after applying filters, verify if there are results
            resultsNotFoundMessage.value = filteredPlanets.value?.isEmpty() == true
        }
    }

    override fun onError(message: String?, validationErrors: Map<String, ArrayList<String>>?) {
        isLoading.value = false
        isRefreshing.value = false
        planets.value = arrayListOf()
    }

    //Order Buttons
    fun toggleSortNameOrder() {
        sortBy = SortBy.NAME
        isDescending = !isDescending
        updateSortedPlanets()
    }

    fun toggleSortedSizeOrder() {
        sortBy = SortBy.DIAMETER
        isDescending = !isDescending
        updateSortedPlanets()
    }

    fun updateSortedPlanets() {
        val listToSort = if (selectedFilters.isEmpty()) {
            planets.value
        } else {
            filteredPlanets.value
        }
        val sortedList = when (sortBy) {
            SortBy.NAME -> {
                val alphabeticSortedList = listToSort?.sortedBy {
                    it.name.lowercase()
                }
                if (isDescending) alphabeticSortedList?.asReversed() else alphabeticSortedList
            }

            SortBy.DIAMETER -> {
                val numericSortedList = listToSort?.sortedBy {
                    it.diameter.toDoubleOrNull() ?: 0.0
                }
                if (isDescending) numericSortedList?.asReversed() else numericSortedList
            }
        }

        if (selectedFilters.isEmpty()) {

            sortedPlanets.value = sortedList
        } else {
            filteredPlanets.value = sortedList
        }
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

    private fun filterPlanetsByProperty(
        filterFunction: (Planet) -> Boolean
    ): List<Planet> {
        return planets.value?.filter { filterFunction(it) } ?: emptyList()
    }

    private fun filterByClimate(): List<Planet> {
        return filterPlanetsByProperty { planet ->
            selectedFilters.isEmpty() || selectedFilters.any { filter ->
                planet.climate.contains(filter)
            }
        }
    }

    private fun filterByTerrain(): List<Planet> {
        return filterPlanetsByProperty { planet ->
            selectedFilters.isEmpty() || selectedFilters.any { filter ->
                planet.terrain.contains(filter)
            }
        }
    }

    fun applyFilters() {
        val climateFiltered = filterByClimate()
        val terrainFiltered = filterByTerrain()

        //check if there are selected filters on both groups
        if (climateFiltered.isNotEmpty() && terrainFiltered.isNotEmpty()) {
            //find the intersection of both groups
            val result = climateFiltered.intersect(terrainFiltered.toSet())
            filteredPlanets.value = result.toList()
            //if just one group is selected
        } else if (climateFiltered.isNotEmpty()) {
            filteredPlanets.value = climateFiltered
        } else if (terrainFiltered.isNotEmpty()) {
            filteredPlanets.value = terrainFiltered
        } else {
            filteredPlanets.value = planets.value
        }

        //after apllying filters, verify if there are results
        resultsNotFoundMessage.value = filteredPlanets.value?.isEmpty() == true
    }

    fun isFilterSelected(filter: String): Boolean {
        return selectedFilters.contains(filter)
    }

    fun resetFilters() {
        selectedFilters.clear()
    }
}
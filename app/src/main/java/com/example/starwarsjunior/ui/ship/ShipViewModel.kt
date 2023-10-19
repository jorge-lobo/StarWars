package com.example.starwarsjunior.ui.ship

import android.app.Application
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.starwarsjunior.data.ship.ShipRepository
import com.example.starwarsjunior.data.ship.objects.Ship
import com.example.starwarsjunior.ui.common.BaseViewModel
import kotlinx.coroutines.launch

class ShipViewModel(application: Application) : BaseViewModel(application),
    LifecycleObserver {

    private var ships = MutableLiveData<List<Ship>?>()
    val filteredShips = MutableLiveData<List<Ship>?>()
    val sortedShips = MutableLiveData<List<Ship>?>()

    val searchQuery = MutableLiveData<String>()
    val resultsNotFoundMessage = MutableLiveData<Boolean>(false)

    private var isDataPreloaded = false

    enum class SortBy {
        NAME, LENGTH
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
        //getShips(forceRefresh)
        getCachedShips(true)
    }

    fun getCachedShips(refresh: Boolean) {
        if (refresh) {
            isLoading.value = true

            noDataAvailable.value = false
            ships.value = emptyList()

            viewModelScope.launch {
                val result = ShipRepository.getCachedShips()

                ships.value = result?.sortedBy { it.name }
                sortedShips.value = ships.value
                isLoading.value = false
                isRefreshing.value = false
                result?.let {
                    if (it.isEmpty()) {
                        noDataAvailable.value = true
                    }
                }
            }
        }
    }

    //SearchBox
    private fun observerSearchQuery() {
        searchQuery.observeForever { query ->
            if (selectedFilters.isEmpty()) {
                val filteredList = ships.value?.filter { ship ->
                    ship.name.contains(query, ignoreCase = true)
                }
                filteredShips.value = filteredList
            } else {
                val filteredList = filteredShips.value?.filter { ship ->
                    ship.name.contains(query, ignoreCase = true)
                }
                filteredShips.value = filteredList
            }

            //after applying filters, verify if there are results
            resultsNotFoundMessage.value = filteredShips.value?.isEmpty() == true
        }
    }

    override fun onError(message: String?, validationErrors: Map<String, ArrayList<String>>?) {
        isLoading.value = false
        isRefreshing.value = false
        ships.value = arrayListOf()
    }

    //Order Buttons
    fun toggleSortNameOrder() {
        sortBy = SortBy.NAME
        isDescending = !isDescending
        updateSortedShips()
    }

    fun toggleSortLengthOrder() {
        sortBy = SortBy.LENGTH
        isDescending = !isDescending
        updateSortedShips()
    }

    fun updateSortedShips() {
        val listToSort = if (selectedFilters.isEmpty()) {
            ships.value
        } else {
            filteredShips.value
        }

        val sortedList = when (sortBy) {
            SortBy.NAME -> {
                if (isDescending) listToSort?.sortedByDescending { it.name }
                else listToSort?.sortedBy { it.name }
            }
            SortBy.LENGTH -> {
                val numericSortedList = listToSort?.sortedBy {
                    it.length.toDoubleOrNull() ?: 0.0
                }
                if (isDescending) numericSortedList?.asReversed() else numericSortedList
            }
        }

        if (selectedFilters.isEmpty()) {
            sortedShips.value = sortedList
        } else {
            filteredShips.value = sortedList
        }
    }

    //add or remove a selected filter
    fun toggleFilter(filter: String) {
        if (selectedFilters.contains(filter)) {
            selectedFilters.remove(filter)
        } else {
            selectedFilters.add(filter)
        }
    }

    //convert hyperdriveRating to Double
    private fun convertHyperdriveRating(ship: Ship): Double {
        return ship.hyperdriveRating.toDoubleOrNull() ?: 0.0
    }

    //convert crew to Int
    private fun convertCrew(ship: Ship): Int {
        val crewString = ship.crew
        val cleanedCrewString = crewString.replace(",", "")
        return cleanedCrewString.toIntOrNull() ?: 0
    }

    private fun filterShipsByProperty(
        filterFunction: (Ship) -> Boolean
    ): List<Ship> {
        return ships.value?.filter { filterFunction(it) } ?: emptyList()
    }

    private fun filterByHyperdriveRating(): List<Ship> {
        return filterShipsByProperty { ship ->
            val hyperdriveRating = convertHyperdriveRating(ship)
            selectedFilters.isEmpty() ||
                    (selectedFilters.contains("slow") && hyperdriveRating < 1.0) ||
                    (selectedFilters.contains("average") && hyperdriveRating == 1.0) ||
                    (selectedFilters.contains("fast") && hyperdriveRating > 1.0)
        }
    }

    private fun filterByCrew(): List<Ship> {
        return filterShipsByProperty { ship ->
            val crew = convertCrew(ship)
            selectedFilters.isEmpty() ||
                    (selectedFilters.contains("little") && crew < 10) ||
                    (selectedFilters.contains("medium") && crew in 10..1000) ||
                    (selectedFilters.contains("large") && crew > 1000)
        }
    }

    fun applyFilters() {
        val hyperdriveRatingFiltered = filterByHyperdriveRating()
        val crewFiltered = filterByCrew()

        //check if there are selected filters on both groups
        if (hyperdriveRatingFiltered.isNotEmpty() && crewFiltered.isNotEmpty()) {
            //find the intersection of both groups
            val result = hyperdriveRatingFiltered.intersect(crewFiltered.toSet())
            filteredShips.value = result.toList()
            //if just one group is selected
        } else if (hyperdriveRatingFiltered.isNotEmpty()) {
            filteredShips.value = hyperdriveRatingFiltered
        } else if (crewFiltered.isNotEmpty()) {
            filteredShips.value = crewFiltered
        } else {
            filteredShips.value = ships.value
        }

        //after applying filters, verify if there are results
        resultsNotFoundMessage.value = filteredShips.value?.isEmpty() == true
    }

    fun isFilterSelected(filter: String): Boolean {
        return selectedFilters.contains(filter)
    }

    fun resetFilters() {
        selectedFilters.clear()
    }
}
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

    var ships = MutableLiveData<List<Ship>?>()
    val filteredShips = MutableLiveData<List<Ship>?>()
    val searchQuery = MutableLiveData<String>()
    private var isDataPreloaded = false

    val sortedShips = MutableLiveData<List<Ship>?>()
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
            val filteredList = ships.value?.filter { ship ->
                ship.name.contains(query, ignoreCase = true)
            }
            filteredShips.value = filteredList
        }
    }

    override fun onError(message: String?, validationErrors: Map<String, ArrayList<String>>?) {
        isLoading.value = false
        isRefreshing.value = false
        ships.value = arrayListOf()
    }

    //Order Buttons
    fun toggleSortNameOrder() {
        sortBy = "name"
        isDescending = !isDescending
        updateSortedShips()
    }

    fun toggleSortLengthOrder() {
        sortBy = "length"
        isDescending = !isDescending
        updateSortedShips()
    }

    private fun updateSortedShips() {
        val sortedList = when (sortBy) {
            "name" -> {
                if (isDescending) sortedShips.value?.sortedByDescending { it.name }
                else sortedShips.value?.sortedBy { it.name }
            }

            "length" -> {
                val numericSortedList = sortedShips.value?.sortedBy {
                    it.length.toDoubleOrNull() ?: 0.0
                }
                if (isDescending) numericSortedList?.asReversed() else numericSortedList
            }
            else -> sortedShips.value
        }
        sortedShips.value = sortedList
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
        /*val filteredList = ships.value?.filter { ship ->
            selectedFilters.isEmpty()  || selectedFilters.contains(ship.TODO)
        }
        filteredShips.value = filteredList*/
    }

    fun isFilterSelected(filter: String): Boolean {
        return selectedFilters.contains(filter)
    }
}
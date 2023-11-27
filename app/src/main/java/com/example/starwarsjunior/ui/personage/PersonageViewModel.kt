package com.example.starwarsjunior.ui.personage

import android.app.Application
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.starwarsjunior.data.personage.PersonageRepository
import com.example.starwarsjunior.data.personage.objects.Personage
import com.example.starwarsjunior.data.personage.objects.Specie
import com.example.starwarsjunior.ui.common.BaseViewModel
import kotlinx.coroutines.launch

class PersonageViewModel(application: Application) : BaseViewModel(application),
    LifecycleObserver {

    private var personages = MutableLiveData<List<Personage>?>()
    val filteredPersonages = MutableLiveData<List<Personage>?>()
    val sortedPersonages = MutableLiveData<List<Personage>?>()

    val searchQuery = MutableLiveData<String>()
    val resultsNotFoundMessage = MutableLiveData<Boolean>(false)

    private var isDataPreloaded = false

    enum class SortBy {
        NAME, BIRTHYEAR
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
        //getPersonages(forceRefresh)
        getCachedPersonages(true)
    }

    fun getCachedPersonages(refresh: Boolean) {
        if (refresh) {
            isLoading.value = true
        }

        noDataAvailable.value = false
        personages.value = emptyList()

        viewModelScope.launch {
            val result = PersonageRepository.getCachedPersonages()
            //sort ist by personage's name
            personages.value = result?.sortedBy { it.name }
            sortedPersonages.value = personages.value
            isLoading.value = false
            isRefreshing.value = false
            result?.let {
                if (it.isEmpty()) {
                    noDataAvailable.value = true
                }
            }
        }
    }

    suspend fun getCachedSpecies(): List<Specie>? {
        return PersonageRepository.getCachedSpecies()
    }

    //SearchBox
    private fun observerSearchQuery() {
        searchQuery.observeForever { query ->
            if (selectedFilters.isEmpty()) {
                val filteredList = personages.value?.filter { personage ->
                    personage.name.contains(query, ignoreCase = true)
                }
                filteredPersonages.value = filteredList
            } else {
                val filteredList = filteredPersonages.value?.filter { personage ->
                    personage.name.contains(query, ignoreCase = true)
                }
                filteredPersonages.value = filteredList
            }

            //after applying filters, verify if there are results
            resultsNotFoundMessage.value = filteredPersonages.value?.isEmpty() == true
        }
    }

    override fun onError(message: String?, validationErrors: Map<String, ArrayList<String>>?) {
        isLoading.value = false
        isRefreshing.value = false
        personages.value = arrayListOf()
    }

    // Order buttons
    fun toggleSortNameOrder() {
        sortBy = SortBy.NAME
        isDescending = !isDescending
        updateSortedPersonages()
    }

    fun toggleSortYearOrder() {
        sortBy = SortBy.BIRTHYEAR
        isDescending = !isDescending
        updateSortedPersonages()
    }

    fun updateSortedPersonages() {
        val listToSort = if (selectedFilters.isEmpty()) {
            personages.value
        } else {
            filteredPersonages.value
        }
        val sortedList = when (sortBy) {
            SortBy.NAME -> {
                val alphabeticSortedList = listToSort?.sortedBy {
                    it.name.lowercase()
                }
                if (isDescending) alphabeticSortedList?.asReversed() else alphabeticSortedList
            }

            SortBy.BIRTHYEAR -> {
                val numericSortedList = listToSort?.sortedBy {
                    it.birthYear.convertBirthYearToDouble()
                }
                if (isDescending) numericSortedList?.asReversed() else numericSortedList
            }
        }
        if (selectedFilters.isEmpty()) {
            sortedPersonages.value = sortedList
        } else {
            filteredPersonages.value = sortedList
        }
    }

// Filter buttons

    //add or remove a selected filter
    fun toggleFilter(filter: String) {
        if (selectedFilters.contains(filter)) {
            selectedFilters.remove(filter)
        } else {
            selectedFilters.add(filter)
        }
    }

    private fun filterPersonagesByProperty(
        filterFunction: (Personage) -> Boolean
    ): List<Personage> {
        return personages.value?.filter { filterFunction(it) } ?: emptyList()
    }

    private fun filterByGender(): List<Personage> {
        return filterPersonagesByProperty { personage ->
            selectedFilters.isEmpty() || selectedFilters.contains(personage.gender)
        }
    }

    fun applyFilters() {
        val genderFiltered = filterByGender()
        filteredPersonages.value = genderFiltered
    }

    fun isFilterSelected(filter: String): Boolean {
        return selectedFilters.contains(filter)
    }

    fun resetFilters() {
        selectedFilters.clear()
    }

    fun String.convertBirthYearToDouble(): Double {
        val numericValue = this.replace("[^0-9.]".toRegex(), "").toDoubleOrNull() ?: 0.0
        return if (this.contains("BBY", ignoreCase = true)) -numericValue else numericValue
    }
}
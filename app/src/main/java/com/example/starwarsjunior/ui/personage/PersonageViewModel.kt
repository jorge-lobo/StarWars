package com.example.starwarsjunior.ui.personage

import android.app.Application
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.starwarsjunior.data.error.CallbackWrapper
import com.example.starwarsjunior.data.personage.PersonageRepository
import com.example.starwarsjunior.data.personage.objects.Personage
import com.example.starwarsjunior.data.personage.objects.PersonageListResponse
import com.example.starwarsjunior.ui.common.BaseViewModel
import kotlinx.coroutines.launch

class PersonageViewModel(application: Application) : BaseViewModel(application),
    LifecycleObserver {

    var personages = MutableLiveData<List<Personage>>()
    val filteredPersonages = MutableLiveData<List<Personage>?>()
    val searchQuery = MutableLiveData<String>()
    private var isDataPreloaded = false

    val sortedPersonages = MutableLiveData<List<Personage>?>()
    private var sortBy = "name"
    private var isDescending = false

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
        getPersonages(refresh = false)
    }

    fun onStart() {
        //getPersonages(forceRefresh)
        getPersonages(true)
    }

    fun getPersonages(refresh: Boolean) {
        if (refresh) {
            isLoading.value = true
        }

        noDataAvailable.value = false
        personages.value = emptyList()

        viewModelScope.launch {
            val personagesResponse = PersonageRepository.getPersonages()
            var result = object : CallbackWrapper<PersonageListResponse>(
                this@PersonageViewModel,
                personagesResponse
            ) {
                override fun onSuccess(data: PersonageListResponse) {
                    // sort list by personage's name
                    personages.value = data.results.sortedBy { it.name }
                    sortedPersonages.value = personages.value
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
            val filteredList = personages.value?.filter { personage ->
                personage.name.contains(query, ignoreCase = true)
            }
            filteredPersonages.value = filteredList
        }
    }

    override fun onError(message: String?, validationErrors: Map<String, ArrayList<String>>?) {
        isLoading.value = false
        isRefreshing.value = false
        personages.value = arrayListOf()
    }

    fun toggleSortNameOrder() {
        sortBy = "name"
        isDescending = !isDescending
        updateSortedPersonages()
    }

    fun toggleSortYearOrder() {
        sortBy = "birthYear"
        isDescending = !isDescending
        updateSortedPersonages()
    }

    private fun updateSortedPersonages() {
        val sortedList = when (sortBy) {
            "name" -> {
                if (isDescending) sortedPersonages.value?.sortedByDescending { it.name }
                else sortedPersonages.value?.sortedBy { it.name }
            }

            "birthYear" -> {
                if (isDescending) sortedPersonages.value?.sortedByDescending { it.birthYear }
                else sortedPersonages.value?.sortedBy { it.birthYear }
            }

            else -> sortedPersonages.value
        }
        sortedPersonages.value = sortedList
    }
}
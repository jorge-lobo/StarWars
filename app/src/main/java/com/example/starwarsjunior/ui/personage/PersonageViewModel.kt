package com.example.starwarsjunior.ui.personage

import android.app.Application
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
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

    var forceRefresh = false

    fun onRefresh() {
        //isRefreshing.value = true
        getPersonages(refresh = false)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        //getPersonages(forceRefresh)
        getPersonages(true)
        forceRefresh = false
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
                    personages.value = data
                    isLoading.value = false
                    isRefreshing.value = false
                    data.let {
                        if (it.isEmpty()) {
                            noDataAvailable.value = true
                        }
                    }
                }
            }
        }
    }

    override fun onError(message: String?, validationErrors: Map<String, ArrayList<String>>?) {
        isLoading.value = false
        isRefreshing.value = false
        personages.value = arrayListOf()
    }
}
package com.example.starwarsjunior.ui

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

class SplashScreenViewModel(application: Application) : BaseViewModel(application),
    LifecycleObserver {

    var personages = MutableLiveData<List<Personage>>()
    val preloadComplete = MutableLiveData<Boolean>()

    fun preloadDataFromAPI(refresh: Boolean) {
        if (refresh) {
            isLoading.value = true
        }

        noDataAvailable.value = false
        personages.value = emptyList()

        viewModelScope.launch {
            val personagesResponse = PersonageRepository.getPersonages()
            var result = object : CallbackWrapper<PersonageListResponse>(
                this@SplashScreenViewModel,
                personagesResponse
            ) {
                override fun onSuccess(data: PersonageListResponse) {
                    personages.value = data.results
                    isLoading.value = false
                    isRefreshing.value = false
                    data.let {
                        if (it.results.isEmpty()) {
                            noDataAvailable.value = true
                        }
                    }
                    preloadComplete.value = true
                }
            }
        }
    }

    override fun onError(message: String?, validationErrors: Map<String, ArrayList<String>>?) {
        isLoading.value = false
        isRefreshing.value = false
    }
}
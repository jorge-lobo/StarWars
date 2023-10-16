package com.example.starwarsjunior.ui.ship.details

import android.app.Application
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.starwarsjunior.data.error.CallbackWrapper
import com.example.starwarsjunior.data.ship.ShipRepository
import com.example.starwarsjunior.data.ship.objects.Ship
import com.example.starwarsjunior.ui.common.BaseViewModel
import kotlinx.coroutines.launch

class ShipDetailViewModel(application: Application) : BaseViewModel(application),
    LifecycleObserver {

    var shipName = MutableLiveData<String>().apply { value = "" }


    fun initialize(shipId: Int) {
        getShipDetails(shipId)
    }

    private fun getShipDetails(shipId: Int) {
        isLoading.value = true

        viewModelScope.launch {
            val shipResponse = ShipRepository.getCachedShip(shipId)
            val result =
                object :
                    CallbackWrapper<Ship?>(this@ShipDetailViewModel, shipResponse) {
                    override fun onSuccess(data: Ship?) {
                        if (data != null) {
                            shipName.value = data.name.lowercase()


                        } else {
                            onError("Unknown error")
                        }
                        isLoading.value = false
                    }
                }
        }
    }

    override fun onError(message: String?, validationErrors: Map<String, ArrayList<String>>?) {
        isLoading.value = false
        isRefreshing.value = false
    }
}
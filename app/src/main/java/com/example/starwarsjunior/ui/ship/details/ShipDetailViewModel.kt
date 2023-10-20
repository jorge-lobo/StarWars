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
import java.text.DecimalFormat

class ShipDetailViewModel(application: Application) : BaseViewModel(application),
    LifecycleObserver {

    var shipName = MutableLiveData<String>().apply { value = "" }
    var shipModel = MutableLiveData<String>().apply { value = "" }
    var shipManufacturer = MutableLiveData<String>().apply { value = "" }
    var shipCostInCredits = MutableLiveData<String>().apply { value = "" }
    var shipLength = MutableLiveData<String>().apply { value = "" }
    var shipMaxAthmospheringSpeed = MutableLiveData<String>().apply { value = "" }
    var shipCrew = MutableLiveData<String>().apply { value = "" }
    var shipPassengers = MutableLiveData<String>().apply { value = "" }
    var shipCargoCapacity = MutableLiveData<String>().apply { value = "" }
    var shipConsumables = MutableLiveData<String>().apply { value = "" }
    var shipHyperdriveRating = MutableLiveData<String>().apply { value = "" }
    var shipMglt = MutableLiveData<String>().apply { value = "" }
    var shipStarshipClass = MutableLiveData<String>().apply { value = "" }

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
                            shipModel.value = data.model.lowercase()
                            shipManufacturer.value = data.manufacturer.lowercase()
                            shipCostInCredits.value = formatter(data.costInCredits.lowercase())
                            shipLength.value = formatter(data.length.lowercase())
                            shipMaxAthmospheringSpeed.value = data.maxAtmospheringSpeed.lowercase()
                            shipCrew.value = formatter(data.crew.lowercase())
                            shipPassengers.value = formatter(data.passengers.lowercase())
                            shipCargoCapacity.value = formatter(data.cargoCapacity.lowercase())
                            shipConsumables.value = data.consumables.lowercase()
                            shipHyperdriveRating.value = data.hyperdriveRating.lowercase()
                            shipMglt.value = data.mGLT.lowercase()
                            shipStarshipClass.value = data.starshipClass.lowercase()

                        } else {
                            onError("Unknown error")
                        }
                        isLoading.value = false
                    }
                }
        }
    }

    fun formatter(n: String): String? = runCatching {
        val numberSeparator = n.replace(",", "").toInt()
        DecimalFormat("#,###").format(numberSeparator)
    }.getOrElse { n }

    override fun onError(message: String?, validationErrors: Map<String, ArrayList<String>>?) {
        isLoading.value = false
        isRefreshing.value = false
    }
}
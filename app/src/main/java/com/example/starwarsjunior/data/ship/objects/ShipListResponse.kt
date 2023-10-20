package com.example.starwarsjunior.data.ship.objects

import com.google.gson.annotations.SerializedName

data class ShipListResponse (
    @SerializedName("results") val results : List<Ship>,
    @SerializedName("count") val count : Int
)
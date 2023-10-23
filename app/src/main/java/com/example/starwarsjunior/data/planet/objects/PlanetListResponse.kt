package com.example.starwarsjunior.data.planet.objects

import com.google.gson.annotations.SerializedName

data class PlanetListResponse (
    @SerializedName("results") val results : List<Planet>,
    @SerializedName("count") val count : Int

)
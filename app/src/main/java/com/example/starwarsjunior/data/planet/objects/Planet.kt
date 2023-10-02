package com.example.starwarsjunior.data.planet.objects

import com.google.gson.annotations.SerializedName

data class Planet (
    @SerializedName("name") val name : String,
    @SerializedName("rotation_period") val rotationPeriod : Int,
    @SerializedName("orbital_period") val orbitalPeriod : Int,
    @SerializedName("diameter") val diameter : Int,
    @SerializedName("climate") val climate : String,
    @SerializedName("gravity") val gravity : String,
    @SerializedName("terrain") val terrain : String,
    @SerializedName("surface_water") val surfaceWater : Int,
    @SerializedName("population") val population : Int,
    @SerializedName("residents") val residents : List<String>,
    @SerializedName("films") val films : List<String>,
    @SerializedName("created") val created : String,
    @SerializedName("edited") val edited : String,
    @SerializedName("url") val url : String
)
package com.example.starwarsjunior.data.personage.objects

import com.google.gson.annotations.SerializedName

data class Personage(

    @SerializedName("name") val name: String,
    @SerializedName("height") val height: Int,
    @SerializedName("mass") val mass: Int,
    @SerializedName("hair_color") val hairColor: String,
    @SerializedName("skin_color") val skinColor: String,
    @SerializedName("eye_color") val eyeColor: String,
    @SerializedName("birth_year") val birthYear: String,
    @SerializedName("gender") val gender: String,
    @SerializedName("homeworld") val homeWorld: String,
    @SerializedName("species") val species: List<String>,
    @SerializedName("url") val url: String

)
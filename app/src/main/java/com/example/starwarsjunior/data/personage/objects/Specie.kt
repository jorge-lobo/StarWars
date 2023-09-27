package com.example.starwarsjunior.data.personage.objects

import com.google.gson.annotations.SerializedName

data class Specie(

    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String
)
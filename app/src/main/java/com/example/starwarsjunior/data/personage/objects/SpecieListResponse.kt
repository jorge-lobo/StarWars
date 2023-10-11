package com.example.starwarsjunior.data.personage.objects

import com.google.gson.annotations.SerializedName

data class SpecieListResponse(
    @SerializedName("results") val results : List<Specie>
)

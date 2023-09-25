package com.example.starwarsjunior.data.personage.objects

import com.google.gson.annotations.SerializedName


data class PersonageListResponse (
    @SerializedName("results") val results : List<Personage>
)
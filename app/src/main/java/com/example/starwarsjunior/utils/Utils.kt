package com.example.starwarsjunior.utils

object Utils {
    //Function to extract the individual number of the URL
    fun extractIdFromUrl(url: String): Int {
        val segments = url.trim('/').split('/')
        return segments.last().toInt()
    }

}
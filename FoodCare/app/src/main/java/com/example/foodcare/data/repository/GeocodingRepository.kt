package com.example.foodcare.data.repository

import com.example.foodcare.data.remote.GeocodingApi

class GeocodingRepository(
    private val api: GeocodingApi
) {

    suspend fun getCoordinates(address: String): Pair<Double, Double>? {
        val result = api.searchAddress(address)

        if (result.isNotEmpty()) {
            val first = result[0]
            return Pair(
                first.lat.toDouble(),
                first.lon.toDouble()
            )
        }

        return null
    }
}
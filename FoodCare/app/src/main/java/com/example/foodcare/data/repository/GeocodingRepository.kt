package com.example.foodcare.data.repository

import com.example.foodcare.data.remote.GeocodingApi

class GeocodingRepository(
    private val api: GeocodingApi
) {

    suspend fun coordenadas(address: String): Pair<Double, Double>? {

        val result = api.searchAddress(address)

        return result.firstOrNull()?.let {
            Pair(
                it.lat.toDouble(),
                it.lon.toDouble()
            )
        }
    }
}
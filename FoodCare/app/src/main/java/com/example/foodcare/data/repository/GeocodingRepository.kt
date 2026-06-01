package com.example.foodcare.data.repository

import com.example.foodcare.data.remote.GeocodingApi
import com.example.foodcare.data.remote.RetrofitClientGeo
import com.example.foodcare.model.Location

class GeocodingRepository() {
    private val api = RetrofitClientGeo.api

    suspend fun getCoordenadas(address: String): Location? {

        val result = api.searchAddress(address)

        return result.firstOrNull()?.let {
            Location(
                it.lat.toDouble(),
                it.lon.toDouble()
            )
        }
    }
}
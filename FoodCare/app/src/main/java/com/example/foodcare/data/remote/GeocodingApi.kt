package com.example.foodcare.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface GeocodingApi {
    @GET("search")
    suspend fun searchAddress(
        @Query("q") query: String,
        @Query("format") format: String = "json"
    ): List<GeocodingDto>

}
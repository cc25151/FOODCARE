package com.example.foodcare.data.api

import retrofit2.http.GET

interface ApiService {

    @GET("api/receitas")
    suspend fun listarReceitas(): List<>

}
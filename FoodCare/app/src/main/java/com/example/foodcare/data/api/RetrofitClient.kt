package com.example.foodcare.data.api

import com.example.foodcare.data.api.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitClient {

    private const val url = "http://10.0.2.2:5269/" // se for testar com celular físico utilizar: "http://IP_COMPUTADOR:5269/"

    val api: ApiService by lazy {

        Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build().create(ApiService::class.java)

    }
}

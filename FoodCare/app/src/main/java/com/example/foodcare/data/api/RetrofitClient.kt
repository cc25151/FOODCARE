package com.example.foodcare.data.api

import com.google.firebase.appdistribution.gradle.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient {

    object RetrofitClient {

        private const val url = "http://localhost:5269/"

        val api: ApiService by lazy {

            Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(
                    GsonConverterFactory.create()
                )
                .build()
                .create(ApiService::class.java)

        }
    }
}
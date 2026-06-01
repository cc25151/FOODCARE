package com.example.foodcare.data.remote

import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.getValue

object RetrofitClientGeo {

    private const val BASE_URL =
        "https://nominatim.openstreetmap.org/"

    private val client = OkHttpClient.Builder()
        .addInterceptor { chain ->

            val request: Request =
                chain.request()
                    .newBuilder()
                    .header(
                        "User-Agent",
                        "FoodCareApp"
                    )
                    .build()

            chain.proceed(request)
        }
        .build()

    val api: GeocodingApi by lazy {

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build()
            .create(GeocodingApi::class.java)
    }
}
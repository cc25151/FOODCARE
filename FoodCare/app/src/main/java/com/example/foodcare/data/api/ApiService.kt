package com.example.foodcare.data.api
import com.example.foodcare.model.*
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.GET

interface ApiService {

    @POST("login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): LoginResposta

}
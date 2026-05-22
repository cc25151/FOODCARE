package com.example.foodcare.data.repository

import com.example.foodcare.data.api.RetrofitClient
import com.example.foodcare.model.*

class LoginRepository {

    suspend fun login(
        email:String,
        senha:String
    ): LoginResposta{

        return RetrofitClient.api.login(
            LoginRequest(
                email=email,
                senha=senha
            )
        )
    }
}
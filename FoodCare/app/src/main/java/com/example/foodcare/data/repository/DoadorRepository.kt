package com.example.foodcare.data.repository

import com.example.foodcare.data.api.RetrofitClient
import com.example.foodcare.model.Doador

class DoadorRepository {
    suspend fun getDoador(idDoador: Int): Doador {
        return RetrofitClient.api.getDoador(idDoador)
    }
}
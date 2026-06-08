package com.example.foodcare.data.repository


import com.example.foodcare.data.api.RetrofitClient
import com.example.foodcare.model.CategoriaResposta

class CategoriaRepository {
    suspend fun BuscarCategorias(): List<CategoriaResposta>{
        return RetrofitClient.api.listarCategorias()
    }
}
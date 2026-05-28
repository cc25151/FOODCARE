package com.example.foodcare.data.repository

import com.example.foodcare.data.api.RetrofitClient
import com.example.foodcare.model.DoacaoRequest
import com.example.foodcare.model.DoacaoResposta
import retrofit2.HttpException

class DoacaoRepository {
    private val api = RetrofitClient.api

    suspend fun cadastrar(request: DoacaoRequest): DoacaoResposta {
        val response = api.cadastrarDoacao(request)

        if (response.isSuccessful) {
            return response.body() ?: throw Exception("Resposta vazia da API")
        } else {
            throw HttpException(response)
        }
    }
}
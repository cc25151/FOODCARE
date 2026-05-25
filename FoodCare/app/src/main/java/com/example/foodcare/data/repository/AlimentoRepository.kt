package com.example.foodcare.data.repository

import com.example.foodcare.data.api.RetrofitClient
import com.example.foodcare.model.*

class AlimentoRepository {
    suspend fun cadastrar(
        nomeDoador: String,
        nome: String,
        descricao: String,
        quantidade: Int,
        validade: String,
        idCategoria: Int
    ): AlimentoResposta {
        val request = AlimentoRequest(
            nome = nome,
            descricao = descricao,
            qntd = quantidade,
            validade = validade,
            idCategoria = idCategoria
        )

        return RetrofitClient.api.cadastrarAlimento(nomeDoador, request)
    }
}
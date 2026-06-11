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

    suspend fun getAlimentos(idUsuario: Int): List<Produto> {
        return RetrofitClient.api.getAlimentos(idUsuario)
    }

    suspend fun getAlimentosDoador(idUsuario: Int): List<Alimento>{
        return RetrofitClient.api.getAlimentosDoador(idUsuario)
    }

    suspend fun atualizarAlimento(idAlimento: Int, novo : Alimento) : Boolean{
        val resposta = RetrofitClient.api.alterarAlimento(idAlimento)
        return resposta.isSuccessful
    }

    suspend fun getProduto(idAlimento: Int) : Produto{
        return RetrofitClient.api.getProduto(idAlimento)
    }
}
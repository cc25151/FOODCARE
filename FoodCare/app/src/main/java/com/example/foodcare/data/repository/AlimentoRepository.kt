package com.example.foodcare.data.repository

import com.example.foodcare.data.api.RetrofitClient
import com.example.foodcare.data.api.SessaoUsuario
import com.example.foodcare.model.*

class AlimentoRepository {
    suspend fun cadastrar(
        idUsuario: Int,
        nome: String,
        descricao: String,
        quantidade: Int,
        validade: String,
        idCategoria: Int
    ): Boolean {
        val request = AlimentoRequest(
            nome = nome,
            descricao = descricao,
            qntd = quantidade,
            validade = validade,
            idCategoria = idCategoria
        )

        return RetrofitClient.api.cadastrarAlimento(idUsuario, request).isSuccessful
    }

    suspend fun getAlimentos(idUsuario: Int): List<Produto> {
        return RetrofitClient.api.getAlimentos(idUsuario)
    }

    suspend fun getAlimentosDoador(idUsuario: Int): List<Alimento>{
        return RetrofitClient.api.getAlimentosDoador(idUsuario)
    }

    suspend fun atualizarAlimento(idAlimento: Int, novo : Alimento) : Boolean{
        val dadosReduzidos = AlimentoAtualizarRequest(novo.qntd, novo.validade, novo.descricao)
        val resposta = RetrofitClient.api.alterarAlimento(idAlimento, dadosReduzidos)
        return resposta.isSuccessful
    }

    suspend fun getProduto(idAlimento: Int) : Produto{

        val tokenFormatado = "Bearer ${SessaoUsuario.token}"

        return RetrofitClient.api.getProduto(tokenFormatado, idAlimento)
    }
}
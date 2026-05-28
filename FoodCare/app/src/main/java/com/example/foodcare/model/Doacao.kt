package com.example.foodcare.model


data class DoacaoRequest(
    val dataDoacao: String,
    val horarioInicial: String,
    val horarioFinal: String,
    val idDoador: Int,
    val idAlimento: Int,
    val idReceptor: Int,
    val avaliacao: Int = 0
)

data class DoacaoResposta(
    val id: Int,
    val alimento: String,
    val status: String
)
package com.example.foodcare.model

data class AlimentoRequest(
    val nome: String,
    val descricao: String,
    val qntd: Int,
    val validade: String,
    val idCategoria: Int
)

data class AlimentoResposta(
    val id: Int,
    val nome: String,
    val quantidade: Int,
    val mensagem: String
)
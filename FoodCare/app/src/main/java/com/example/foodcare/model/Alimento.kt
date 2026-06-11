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

//model para o feed
data class Produto(
    val id: Int = 0,
    val nome: String = "",
    val idCategoria: Int = 0,
    val idDoador: Int = 0,
    val descricao: String = "",
    val distancia: Double = 0.0,
    val validade: String = ""
)

// model para alimentos doador
data class Alimento(
    val id: Int,
    val nome: String,
    val idCategoria: Int,
    val categoria: String,
    val descricao: String,
    val qntd: Int,
    val validade: String

)
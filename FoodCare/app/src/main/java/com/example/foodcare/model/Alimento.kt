package com.example.foodcare.model

data class AlimentoRequest(
    val nome: String,
    val descricao: String,
    val qntd: Int,
    val validade: String,
    val idCategoria: Int
)

data class AlimentoAtualizarRequest(
    val qntd: Int,
    val validade: String,
    val descricao: String
)

//model para o feed
data class Produto(
    val idAlimento: Int = 0,
    val nome: String = "",
    val idCategoria: Int = 0,
    val idDoador: Int = 0,
    val descricao: String = "",
    val distancia: Double = 0.0,
    val validade: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
)

// model para alimentos doador
data class Alimento(
    val idAlimento: Int,
    val nome: String,
    val idCategoria: Int,
    val categoria: String,
    val descricao: String,
    val qntd: Int,
    val validade: String

)
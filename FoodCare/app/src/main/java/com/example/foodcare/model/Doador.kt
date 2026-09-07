package com.example.foodcare.model

data class Doador(
    val idDoador: Int,
    val nome: String,
    val idUsuario: Int,
    val pontuacao: Double?,
    val endereco: String,
)
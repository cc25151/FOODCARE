package com.example.foodcare.model

data class LoginRequest(
    val email: String,
    val senha: String
)

data class LoginResposta(
    val idUsuario: Int,
    val nome: String,
    val token: String
)
package com.example.foodcare.model

data class CadastroRequest(
    val nome : String,
    val email: String,
    val senha: String,
    val tipoPessoa : String,
    val documento : String
)

data class CadastroResposta(
    val idUsuario: Int
)

data class DoadorRequest(
    val idUsuario: Int
)

data class ReceptorRequest(
    val idUsuario: Int
)

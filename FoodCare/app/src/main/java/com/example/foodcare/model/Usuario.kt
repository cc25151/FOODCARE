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

data class CompletarPerfilRequest(
    val nome: String,
    val email: String,
    val cep: String?,
    val cidade: String?,
    val bairro: String?,
    val rua: String?,
    val numero: String?,

    val latitude: Double?,
    val longitude: Double?
)
data class LoginRequest(
    val email: String,
    val senha: String
)

data class LoginResposta(
    val idUsuario: Int,
    val nome: String,
    val token: String,
    val tipoUsuario : String
)

data class Usuario(
    val nome: String,
    val email: String,
    val documento: String,
    val tipoPessoa: String,
    val cep: String?,
    val cidade: String?,
    val bairro: String?,
    val rua: String?,
    val numero: String?
)

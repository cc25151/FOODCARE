package com.example.foodcare.data.repository

import com.example.foodcare.data.api.RetrofitClient
import com.example.foodcare.model.*

class UsuarioRepository {

    suspend fun login(
        email:String,
        senha:String
    ): LoginResposta{

        return RetrofitClient.api.login(
            LoginRequest(
                email=email,
                senha=senha
            )
        )
    }

    suspend fun cadastro(
        nome : String,
        email : String,
        senha : String,
        tipoPessoa : String,
        documento : String
    ): CadastroResposta{

        return RetrofitClient.api.cadastro(
            CadastroRequest(
                nome = nome,
                email = email,
                senha = senha,
                tipoPessoa = tipoPessoa,
                documento = documento
            )
        )
    }

    suspend fun cadastrarDoador(
        idUsuario: Int
    ): Boolean {

        val resposta =
            RetrofitClient.api
                .cadastroDoador(
                    DoadorRequest(idUsuario)
                )

        return resposta.isSuccessful
    }

    suspend fun cadastrarReceptor(
        idUsuario: Int
    ): Boolean {

        val resposta =
            RetrofitClient.api
                .cadastroReceptor(
                    ReceptorRequest(idUsuario)
                )

        return resposta.isSuccessful
    }

    suspend fun completarPerfil(
        id: Int,
        nome: String,
        email: String,
        cep: String,
        cidade: String,
        bairro: String,
        rua: String,
        numero: String,
        latitude: Double,
        longitude: Double
    ): Boolean {

        return RetrofitClient.api.completarPerfil(
            id,

            CompletarPerfilRequest(
                nome = nome,
                email = email,
                cep = cep,
                cidade = cidade,
                bairro = bairro,
                rua = rua,
                numero = numero,
                latitude = latitude,
                longitude = longitude
            )

        ).isSuccessful
    }

    suspend fun getDados(
        id: Int
    ): Usuario{ return RetrofitClient.api.dados(id)}
}
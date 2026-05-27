package com.example.foodcare.data.repository

import com.example.foodcare.data.api.RetrofitClient
import com.example.foodcare.model.*
import kotlin.String

class CadastroRepository {

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
}

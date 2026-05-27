package com.example.foodcare.data.repository

import com.example.foodcare.data.api.RetrofitClient
import com.example.foodcare.model.*

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

    }

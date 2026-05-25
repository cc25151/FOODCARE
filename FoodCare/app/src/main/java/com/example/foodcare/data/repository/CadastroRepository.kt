package com.example.foodcare.data.repository

import com.example.foodcare.data.api.RetrofitClient
import com.example.foodcare.model.CadastroRequest
import com.example.foodcare.model.CadastroResposta

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
    }

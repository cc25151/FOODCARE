package com.example.foodcare.data.api

import com.example.foodcare.model.*
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path // Não esqueça de importar o Path!

interface ApiService {
    @POST("cadastro")
    suspend fun  cadastro(
        @Body CadastroRequest : CadastroRequest
    ) : CadastroResposta
    @POST("login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): LoginResposta

    @POST("alimentos/doador/{nomeDoador}")
    suspend fun cadastrarAlimento(
        @Path("nomeDoador") nomeDoador: String,
        @Body alimento: AlimentoRequest
    ): AlimentoResposta
}
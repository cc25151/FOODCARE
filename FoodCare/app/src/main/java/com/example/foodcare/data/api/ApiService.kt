package com.example.foodcare.data.api

import com.example.foodcare.model.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @POST("usuarios/cadastro")
    suspend fun  cadastro(
        @Body CadastroRequest : CadastroRequest
    ) : CadastroResposta

    @POST("doadores/cadastro")
    suspend fun cadastroDoador(
        @Body doador: DoadorRequest
    ): Response<Unit>

    @POST("receptores/cadastro")
    suspend fun cadastroReceptor(
        @Body receptor: ReceptorRequest
    ): Response<Unit>

    @POST("login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): LoginResposta

    @POST("alimentos/doador/{nomeDoador}")
    suspend fun cadastrarAlimento(
        @Path("nomeDoador") nomeDoador: String,
        @Body alimento: AlimentoRequest
    ): AlimentoResposta

    @PATCH("completar-perfil/{id}")
    suspend fun completarPerfil(

        @Path("id")
        id: Int,

        @Body
        dados: CompletarPerfilRequest

    ): Response<Unit>


    @POST("doacoes")
    suspend fun cadastrarDoacao(
        @Body doacao: DoacaoRequest
    ): Response<DoacaoResponse>
}
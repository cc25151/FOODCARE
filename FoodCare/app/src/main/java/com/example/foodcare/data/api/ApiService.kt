package com.example.foodcare.data.api

import com.example.foodcare.model.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @GET("doadores/{idDoador}")
    suspend fun getDoadorPorId(@Path("idDoador") idDoador: Int): Doador

    @GET("receptores/{idReceptor}")
    suspend fun getReceptorPorId(@Path("idReceptor") idReceptor: Int): Receptor

    @POST("usuarios/cadastro")
    suspend fun cadastro(
        @Body request: CadastroRequest
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


    @POST("alimentos/doador/{idUsuario}")
    suspend fun cadastrarAlimento(
        @Path("idUsuario") idUsuario: Int,
        @Body alimento: AlimentoRequest
    ): Response<Unit>

    @PATCH("usuarios/completar-perfil/{id}")
    suspend fun completarPerfil(

        @Path("id")
        id: Int,

        @Body
        dados: CompletarPerfilRequest

    ): Response<Unit>

    @GET("usuarios/{id}")
    suspend fun dados(
        @Path("id") id: Int
    ): Usuario


    @POST("doacoes")
    suspend fun cadastrarDoacao(
        @Body doacao: DoacaoRequest
    ): Response<DoacaoResposta>

    @GET("categorias/{nome}")
    suspend fun buscarCategoriaPorNome(
        @Path("nome") nome: String
    ): Response<CategoriaResposta>

    @GET("categorias")
    suspend fun listarCategorias(): List<CategoriaResposta>

    @GET("alimentos/feed/{idUsuario}")
    suspend fun getAlimentos(
        @Path("idUsuario") idUsuario: Int
    ): List<Produto>

    @GET("alimentos/doador/{idUsuario}")
    suspend fun getAlimentosDoador(
        @Path("idUsuario") idUsuario: Int
    ) : List<Alimento>

    @PATCH("alimentos/alterar/{idAlimento}")
    suspend fun alterarAlimento(
        @Path("idAlimento") idAlimento: Int,
        @Body alimento: AlimentoAtualizarRequest
    ) : Response<Unit>

    @GET("alimentos/id/{idAlimento}")
    suspend fun getProduto(
        @Header("Authorization") token: String,
        @Path("idAlimento") idAlimento: Int
    ) : Produto

    @GET("doadores/{idDoador}")
    suspend fun getDoador(
        @Path("idDoador") idDoador: Int
    ) : Doador
}
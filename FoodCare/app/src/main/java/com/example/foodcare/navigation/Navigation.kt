package com.example.foodcare.navigation

object Routes {
    const val TELA_INICIAL      = "tela_inicial"
    const val QUEM_SOMOS        = "quem_somos"
    const val LOGIN             = "login"
    const val CADASTRO = "cadastro/{tipoUsuario}"

    const val MAIN              = "main"
    const val FINALIDADE = "finalidade"

    const val PRODUTO           = "produto/{produtoId}"
    const val PRODUTO_REQ       = "produto_req/{produtoId}"
    const val PERFIL_PROPRIO    = "perfil"
    const val PERFIL_DOADOR_PUBLICO = "perfil_doador_publico/{doadorId}"


    const val CADASTRAR_ALIMENTO = "cadastrar_alimento"

    const val CADASTRAR_DOACAO   = "cadastrar_doacao"

    fun produto(id: Int)             = "produto/$id"
    fun produto_req(id: Int)         = "produto_req/$id"
    fun perfilDoadorPublico(id: Int) = "perfil_doador_publico/$id"
    fun cadastro(tipoUsuario:String) = "cadastro/$tipoUsuario"

}
package com.example.foodcare.navigation

object Routes {
    const val TELA_INICIAL      = "tela_inicial"
    const val QUEM_SOMOS        = "quem_somos"
    const val LOGIN             = "login"
    const val CADASTRO_RECEPTOR = "cadastro_receptor"

    // Hub pós-login com BottomNavigationBar
    const val MAIN              = "main"

    const val PRODUTO           = "produto/{produtoId}"
    const val PRODUTO_REQ       = "produto_req/{produtoId}"
    const val PERFIL_PROPRIO    = "perfil"
    const val PERFIL_DOADOR_PUBLICO = "perfil_doador_publico/{doadorId}"

    // ── Fluxo de nova doação (doador) ─────────────────────────────────────────
    // Passo 1: dados do alimento (nome, descrição, qtd, validade, categoria)
    const val CADASTRAR_ALIMENTO = "cadastrar_alimento"
    // Passo 2: dados da doação (data, horário início, horário fim)
    // Os dados do passo 1 são mantidos no ViewModel compartilhado entre os passos.
    const val CADASTRAR_DOACAO   = "cadastrar_doacao"

    fun produto(id: Int)             = "produto/$id"
    fun produto_req(id: Int)         = "produto_req/$id"
    fun perfilDoadorPublico(id: Int) = "perfil_doador_publico/$id"
}
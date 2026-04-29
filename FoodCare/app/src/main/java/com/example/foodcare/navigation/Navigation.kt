package com.example.foodcare.navigation

object Routes {
    const val TELA_INICIAL       = "tela_inicial"
    const val QUEM_SOMOS         = "quem_somos"
    const val LOGIN              = "login"
    const val CADASTRO_RECEPTOR  = "cadastro_receptor"
    const val HOME_FEED          = "home_feed"
    const val PRODUTO            = "produto/{produtoId}"

    fun produto(id: Int) = "produto/$id"
}

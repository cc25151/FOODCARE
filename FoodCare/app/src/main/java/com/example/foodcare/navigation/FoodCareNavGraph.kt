package com.example.foodcare.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.foodcare.data.api.SessaoUsuario
import com.example.foodcare.view.*
import com.example.foodcare.model.*

@Composable
fun FoodCareNavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController    = navController,
        startDestination = Routes.TELA_INICIAL
    ) {

        composable(Routes.TELA_INICIAL) {
            TelaInicial(
                onEntrarClick    = { navController.navigate(Routes.LOGIN) },
                onCadastrarClick = { navController.navigate(Routes.FINALIDADE) },
                onQuemSomosClick = { navController.navigate(Routes.QUEM_SOMOS) }
            )
        }


        composable(Routes.QUEM_SOMOS) {
            TelaQuemSomos(onVoltar = { navController.popBackStack() })
        }

        composable(Routes.LOGIN) {
            TelaLogin(
                onLoginClick  = {
                    navController.navigate(Routes.HOMEFEEDPRINCIPAL) {
                        popUpTo(Routes.TELA_INICIAL) { inclusive = false }
                    }
                },

                onCriarConta  = { navController.navigate(Routes.CADASTRO) },
                onVoltar      = { navController.popBackStack() }
            )
        }
        composable(Routes.FINALIDADE){
            TelaFinalidade(
                onAvancar = { navController.navigate(Routes.CADASTRO)},
                onVoltar = {navController.popBackStack()}
            )

        }

        composable(Routes.CADASTRO) {
            TelaCadastro(
                onEntrar = {
                    navController.navigate(Routes.HOMEFEEDPRINCIPAL) {
                        popUpTo(Routes.TELA_INICIAL) { inclusive = false }
                    }
                },
                onJaTenhoConta = { navController.navigate(Routes.LOGIN) },
                onVoltar       = { navController.popBackStack() }
            )
        }

        composable(Routes.HOMEFEEDPRINCIPAL) {
            TelaHomeFeedPrincipal(
                doacoesPendentes      = emptyList(),
                onProdutoClick        = { id -> navController.navigate(Routes.produto(id)) },
                onPerfilClick         = { navController.navigate(Routes.PERFIL_PROPRIO) },
                onRegistrarNovaDoacao = { navController.navigate(Routes.CADASTRAR_ALIMENTO) }
            )
        }

        composable(
            route     = Routes.PRODUTO,
            arguments = listOf(navArgument("produtoId") { type = NavType.IntType })
        ) { back ->
            val id = back.arguments?.getInt("produtoId") ?: 0
            TelaProduto(
                produtoId          = id,
                onVoltar           = { navController.popBackStack() },
                onQueroEsteProduto = { navController.navigate(Routes.produto_req(id)) }
            )
        }

        composable(
            route     = Routes.PRODUTO_REQ,
            arguments = listOf(navArgument("produtoId") { type = NavType.IntType })
        ) { back ->
            val id = back.arguments?.getInt("produtoId") ?: 0
            TelaProdutoRequisitado(
                produtoId = id,
                onVoltar  = {
                    navController.navigate(Routes.HOMEFEEDPRINCIPAL) {
                        popUpTo(Routes.HOMEFEEDPRINCIPAL) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.PERFIL_PROPRIO) {
            TelaPerfil(
                onVoltar       = { navController.popBackStack() },
                onEditarPerfil = {  },
                onLogout       = {
                    navController.navigate(Routes.TELA_INICIAL) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route     = Routes.PERFIL_DOADOR_PUBLICO,
            arguments = listOf(navArgument("doadorId") { type = NavType.IntType })
        ) {
            TelaPerfilDoadorPublico(
                onVoltar = { navController.popBackStack() }
            )
        }


        composable(Routes.CADASTRAR_ALIMENTO) {
            TelaCadastrarAlimento(
                onVoltar   = { navController.popBackStack() },
                onProximo  = { alimentoData ->
                    navController.navigate(Routes.CADASTRAR_DOACAO)
                }
            )
        }

        composable(Routes.CADASTRAR_DOACAO) {
            TelaCadastrarDoacao(
                alimentoFormData = AlimentoFormData(0, "", "", 0, "", 0),
                onVoltar         = { navController.popBackStack() },
                onConfirmar      = { doacaoData ->
                    navController.navigate(Routes.HOMEFEEDPRINCIPAL) {
                        popUpTo(Routes.HOMEFEEDPRINCIPAL) { inclusive = true }
                    }
                }
            )
        }
    }
}
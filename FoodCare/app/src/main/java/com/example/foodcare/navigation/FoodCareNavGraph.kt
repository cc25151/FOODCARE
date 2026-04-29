package com.example.foodcare.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.foodcare.view.*

@Composable
fun FoodCareNavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Routes.TELA_INICIAL
    ) {
        composable(Routes.TELA_INICIAL) {
            TelaInicial(
                onEntrarClick    = { navController.navigate(Routes.LOGIN) },
                onCadastrarClick = { navController.navigate(Routes.CADASTRO_RECEPTOR) }
            )
        }

        composable(Routes.QUEM_SOMOS) {
            TelaQuemSomos(
                onVoltar = { navController.popBackStack() }
            )
        }

        composable(Routes.LOGIN) {
            TelaLogin(
                onLoginClick         = { navController.navigate(Routes.HOME_FEED) {
                    popUpTo(Routes.TELA_INICIAL) { inclusive = false }
                }},
                onGoogleLogin        = { navController.navigate(Routes.HOME_FEED) {
                    popUpTo(Routes.TELA_INICIAL) { inclusive = false }
                }},
                onCriarConta         = { navController.navigate(Routes.CADASTRO_RECEPTOR) },
                onVoltar             = { navController.popBackStack() }
            )
        }

        composable(Routes.CADASTRO_RECEPTOR) {
            TelaCadastroReceptor(
                onEntrar = { navController.navigate(Routes.HOME_FEED) {
                    popUpTo(Routes.TELA_INICIAL) { inclusive = false }
                }},
                onJaTenhoConta = { navController.navigate(Routes.LOGIN) },
                onVoltar       = { navController.popBackStack() }
            )
        }

        composable(Routes.HOME_FEED) {
            TelaHomeFeed(
                onProdutoClick = { id -> navController.navigate(Routes.produto(id)) }
            )
        }

        composable(
            route = Routes.PRODUTO,
            arguments = listOf(navArgument("produtoId") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("produtoId") ?: 0
            TelaProduto(
                produtoId = id,
                onVoltar  = { navController.popBackStack() }
            )
        }
    }
}

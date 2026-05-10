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
        navController    = navController,
        startDestination = Routes.TELA_INICIAL
    ) {

        // ── Tela Inicial ──────────────────────────────────────────────────────
        composable(Routes.TELA_INICIAL) {
            TelaInicial(
                onEntrarClick    = { navController.navigate(Routes.LOGIN) },
                onCadastrarClick = { navController.navigate(Routes.CADASTRO_RECEPTOR) },
                onQuemSomosClick = { navController.navigate(Routes.QUEM_SOMOS) }
            )
        }

        // ── Quem Somos ────────────────────────────────────────────────────────
        composable(Routes.QUEM_SOMOS) {
            TelaQuemSomos(onVoltar = { navController.popBackStack() })
        }

        // ── Login ─────────────────────────────────────────────────────────────
        composable(Routes.LOGIN) {
            TelaLogin(
                onLoginClick  = {
                    navController.navigate(Routes.MAIN) {
                        popUpTo(Routes.TELA_INICIAL) { inclusive = false }
                    }
                },
                onGoogleLogin = {
                    navController.navigate(Routes.MAIN) {
                        popUpTo(Routes.TELA_INICIAL) { inclusive = false }
                    }
                },
                onCriarConta  = { navController.navigate(Routes.CADASTRO_RECEPTOR) },
                onVoltar      = { navController.popBackStack() }
            )
        }

        // ── Cadastro Receptor ─────────────────────────────────────────────────
        composable(Routes.CADASTRO_RECEPTOR) {
            TelaCadastroReceptor(
                onEntrar = {
                    navController.navigate(Routes.MAIN) {
                        popUpTo(Routes.TELA_INICIAL) { inclusive = false }
                    }
                },
                onJaTenhoConta = { navController.navigate(Routes.LOGIN) },
                onVoltar       = { navController.popBackStack() }
            )
        }

        // ── Main (hub com BottomNavigationBar) ────────────────────────────────
        // Alterna entre TelaHomeFeed (receptor) e TelaHomeFeedDoador via BottomNav.
        // TODO: passar nomeUsuario e nomeDoador do ViewModel / sessão
        composable(Routes.MAIN) {
            MainScreen(
                nomeUsuario                 = "",   // TODO: ViewModel
                nomeDoador                  = "",   // TODO: ViewModel
                doacoesPendentes            = emptyList(), // TODO: ViewModel
                onProdutoClick              = { id -> navController.navigate(Routes.produto(id)) },
                onPerfilClick               = { navController.navigate(Routes.PERFIL_PROPRIO) },
                onRegistrarNovaDoacao       = { navController.navigate(Routes.CADASTRAR_ALIMENTO) }
            )
        }

        // ── Produto (detalhe) ─────────────────────────────────────────────────
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

        // ── Produto Requisitado ───────────────────────────────────────────────
        composable(
            route     = Routes.PRODUTO_REQ,
            arguments = listOf(navArgument("produtoId") { type = NavType.IntType })
        ) { back ->
            val id = back.arguments?.getInt("produtoId") ?: 0
            TelaProdutoRequisitado(
                produtoId = id,
                onVoltar  = {
                    navController.navigate(Routes.MAIN) {
                        popUpTo(Routes.MAIN) { inclusive = true }
                    }
                }
            )
        }

        // ── Perfil próprio ────────────────────────────────────────────────────
        composable(Routes.PERFIL_PROPRIO) {
            TelaPerfil(
                onVoltar       = { navController.popBackStack() },
                onEditarPerfil = { /* TODO: tela de edição de perfil */ },
                onLogout       = {
                    navController.navigate(Routes.TELA_INICIAL) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        // ── Perfil público de doador ───────────────────────────────────────────
        composable(
            route     = Routes.PERFIL_DOADOR_PUBLICO,
            arguments = listOf(navArgument("doadorId") { type = NavType.IntType })
        ) {
            TelaPerfilDoadorPublico(
                onVoltar = { navController.popBackStack() }
            )
        }

        // ── Fluxo de nova doação ──────────────────────────────────────────────

        // Passo 1: Dados do alimento
        // TODO: injetar CadastrarDoacaoViewModel compartilhado entre os dois passos
        //   val vm: CadastrarDoacaoViewModel = hiltViewModel()
        composable(Routes.CADASTRAR_ALIMENTO) {
            TelaCadastrarAlimento(
                categorias = emptyList(), // TODO: vm.categorias.collectAsState()
                onVoltar   = { navController.popBackStack() },
                onProximo  = { alimentoData ->
                    // TODO: vm.salvarAlimentoTemp(alimentoData)
                    navController.navigate(Routes.CADASTRAR_DOACAO)
                }
            )
        }

        // Passo 2: Dados da doação
        // TODO: injetar mesmo ViewModel do passo 1 e recuperar AlimentoFormData
        composable(Routes.CADASTRAR_DOACAO) {
            TelaCadastrarDoacao(
                alimentoFormData = AlimentoFormData("", "", 0, "", 0), // TODO: vm.alimentoTemp
                onVoltar         = { navController.popBackStack() },
                onConfirmar      = { doacaoData ->
                    // TODO: vm.cadastrarDoacao(doacaoData) → chama API → navega ao Main
                    navController.navigate(Routes.MAIN) {
                        popUpTo(Routes.MAIN) { inclusive = true }
                    }
                }
            )
        }
    }
}
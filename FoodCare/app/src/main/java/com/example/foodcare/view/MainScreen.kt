package com.example.foodcare.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ─── Paleta ───────────────────────────────────────────────────────────────────
private val MSRed      = Color(0xFFD32F2F)
private val MSWhite    = Color(0xFFFFFFFF)
private val MSOffWhite = Color(0xFFF5F5F5)
private val MSGray     = Color(0xFF9E9E9E)

// ─── Abas da barra inferior ───────────────────────────────────────────────────
private enum class NavTab(val label: String, val icon: ImageVector) {
    RECEPTOR("Receptor",  Icons.Default.Home),
    DOADOR  ("Doador",    Icons.Default.VolunteerActivism)
}

// ─────────────────────────────────────────────────────────────────────────────
//  MainScreen
//
//  Scaffold raiz com BottomNavigationBar.
//  Alterna entre TelaHomeFeed (receptor) e TelaHomeFeedDoador.
//
//  TODO (implementar posteriormente):
//    · Mostrar/ocultar abas conforme tipoUsuario vindo da API
//    · Passar nomeUsuario / nomeDoador / doacoesPendentes do ViewModel
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun MainScreen(
    // Dados vindos do ViewModel / API
    nomeUsuario: String                  = "",
    nomeDoador: String                   = "",
    doacoesPendentes: List<DoacaoPendenteUi> = emptyList(),

    // Callbacks de navegação — receptor
    onProdutoClick: (Int) -> Unit        = {},
    onPerfilClick: () -> Unit            = {},

    // Callbacks de navegação — doador
    onRegistrarNovaDoacao: () -> Unit    = {}
) {
    var tabAtiva by remember { mutableStateOf(NavTab.RECEPTOR) }

    Scaffold(
        containerColor = MSOffWhite,
        bottomBar = {
            NavigationBar(
                containerColor = MSWhite,
                tonalElevation = 0.dp
            ) {
                NavTab.entries.forEach { tab ->
                    val selecionado = tabAtiva == tab
                    NavigationBarItem(
                        selected = selecionado,
                        onClick  = { tabAtiva = tab },
                        icon = {
                            Icon(
                                imageVector        = tab.icon,
                                contentDescription = tab.label
                            )
                        },
                        label = {
                            Text(
                                text       = tab.label,
                                fontSize   = 11.sp,
                                fontWeight = if (selecionado) FontWeight.SemiBold
                                else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor   = MSRed,
                            selectedTextColor   = MSRed,
                            unselectedIconColor = MSGray,
                            unselectedTextColor = MSGray,
                            indicatorColor      = MSRed.copy(alpha = 0.10f)
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (tabAtiva) {
                NavTab.RECEPTOR ->
                    TelaHomeFeed(
                        nomeUsuario    = nomeUsuario,
                        onProdutoClick = onProdutoClick,
                        onPerfilClick  = onPerfilClick
                    )

                NavTab.DOADOR ->
                    TelaHomeFeedDoador(
                        nomeDoador            = nomeDoador,
                        doacoes               = doacoesPendentes,
                        onPerfilClick         = onPerfilClick,
                        onRegistrarNovaDoacao = onRegistrarNovaDoacao
                    )
            }
        }
    }
}
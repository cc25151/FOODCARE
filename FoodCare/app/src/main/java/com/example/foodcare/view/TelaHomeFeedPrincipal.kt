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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foodcare.ui.theme.*


private enum class NavTab(val label: String, val icon: ImageVector) {
    RECEPTOR("Receptor",  Icons.Default.Home),
    DOADOR  ("Doador",    Icons.Default.VolunteerActivism)
}

@Composable
fun TelaHomeFeedPrincipal(

    nomeUsuario: String                  = "",
    nomeDoador: String                   = "",
    doacoesPendentes: List<DoacaoPendenteUi> = emptyList(),
    onProdutoClick: (Int) -> Unit        = {},
    onPerfilClick: () -> Unit            = {},
    onRegistrarNovaDoacao: () -> Unit    = {}
) {
    var tabAtiva by remember { mutableStateOf(NavTab.RECEPTOR) }

    Scaffold(
        containerColor = BrancoAlt,
        bottomBar = {
            NavigationBar(
                containerColor = Branco,
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
        selectedIconColor   = Vermelho,
        selectedTextColor   = Vermelho,
        unselectedIconColor = Cinza,
        unselectedTextColor = Cinza,
        indicatorColor      = Vermelho.copy(alpha = 0.10f)
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
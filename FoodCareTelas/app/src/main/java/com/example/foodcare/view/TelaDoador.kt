package com.example.foodcare.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ─── Dados fictícios de doações pendentes ──────────────────────────────────
private data class DoacaoPendente(
    val id: Int,
    val nomeProduto: String,
    val emoji: String,
    val receptor: String,
    val status: String,
    val statusColor: Color
)

private val doacoesPendentes = listOf(
    DoacaoPendente(1, "Cesta Básica",    "🛒", "João Silva",   "Aguardando retirada", Color(0xFFF57C00)),
    DoacaoPendente(2, "Marmita Média",   "🍱", "Maria Santos", "Confirmada",          Color(0xFF388E3C)),
    DoacaoPendente(3, "Pacote de Arroz", "🌾", "Carlos Lima",  "Em análise",          Color(0xFF1565C0))
)

// ─── Tela do Doador ────────────────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaDoador(
    nomeDoador: String = "Doador(a) X",
    onRegistrarDoacao: () -> Unit = {},
    onVerificarPendentes: () -> Unit = {},
    onPerfilClick: () -> Unit = {}
) {
    Scaffold(
        containerColor = FoodCareOffWhite,
        // ── Cabeçalho branco ────────────────────────────────────────
        topBar = {
            Surface(
                color          = FoodCareWhite,
                shadowElevation = 3.dp,
                tonalElevation  = 0.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment     = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text       = "Bem-vindo,",
                            fontSize   = 13.sp,
                            color      = FoodCareSubText
                        )
                        Text(
                            text       = nomeDoador,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize   = 20.sp,
                            color      = FoodCareTextDark,
                            letterSpacing = (-0.3).sp
                        )
                    }

                    // Avatar do doador
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(FoodCareOffWhite)
                            .border(2.dp, FoodCareRed.copy(alpha = 0.3f), CircleShape)
                            .clickable { onPerfilClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector        = Icons.Default.Person,
                            contentDescription = "Perfil",
                            tint               = FoodCareTextDark,
                            modifier           = Modifier.size(22.dp)
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(FoodCareOffWhite),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {

            // ── Card principal vermelho (banner FoodCare) ────────────
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    FoodCareRed,
                                    Color(0xFFB71C1C)
                                )
                            )
                        )
                        .padding(24.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally,
                           modifier = Modifier.fillMaxWidth()) {
                        // Ícone / logo central
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(CircleShape)
                                .background(FoodCareWhite.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("🛍️", fontSize = 36.sp)
                        }

                        Spacer(Modifier.height(12.dp))

                        Text(
                            text       = "FoodCare",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize   = 26.sp,
                            color      = FoodCareWhite,
                            letterSpacing = (-0.5).sp
                        )

                        Spacer(Modifier.height(4.dp))

                        Text(
                            text     = "Cada doação transforma uma vida",
                            fontSize = 13.sp,
                            color    = FoodCareWhite.copy(alpha = 0.8f)
                        )

                        Spacer(Modifier.height(20.dp))

                        // Estatísticas rápidas
                        Row(
                            modifier              = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            EstatisticaItem("12", "Doações\nrealizadas")
                            VerticalDivider()
                            EstatisticaItem("3",  "Pendentes")
                            VerticalDivider()
                            EstatisticaItem("47", "Pessoas\nimpactadas")
                        }
                    }
                }
            }

            // ── Botões de ação ──────────────────────────────────────
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    BotaoAcao(
                        texto  = "Registrar nova doação",
                        icone  = Icons.Default.AddBox,
                        fundo  = FoodCareRed,
                        texto_cor = FoodCareWhite,
                        onClick = onRegistrarDoacao
                    )

                    BotaoAcao(
                        texto  = "Verificar doações pendentes",
                        icone  = Icons.Default.Pending,
                        fundo  = FoodCareWhite,
                        texto_cor = FoodCareRed,
                        borda  = true,
                        onClick = onVerificarPendentes
                    )
                }
            }

            // ── Seção de doações recentes ────────────────────────────
            item {
                Spacer(Modifier.height(20.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment     = Alignment.CenterVertically
                ) {
                    Text(
                        text       = "Doações Recentes",
                        fontWeight = FontWeight.Bold,
                        fontSize   = 17.sp,
                        color      = FoodCareTextDark
                    )
                    Text(
                        text       = "Ver histórico",
                        fontSize   = 12.sp,
                        color      = FoodCareRed,
                        fontWeight = FontWeight.Medium
                    )
                }
                Spacer(Modifier.height(8.dp))
            }

            // ── Cards das doações pendentes ──────────────────────────
            items(doacoesPendentes) { doacao ->
                DoacaoCard(doacao)
            }

            item { Spacer(Modifier.height(16.dp)) }
        }
    }
}

// ─── Componente: estatística no banner ────────────────────────────────────
@Composable
private fun EstatisticaItem(valor: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text       = valor,
            fontWeight = FontWeight.ExtraBold,
            fontSize   = 22.sp,
            color      = FoodCareWhite
        )
        Text(
            text     = label,
            fontSize = 10.sp,
            color    = FoodCareWhite.copy(alpha = 0.75f)
        )
    }
}

@Composable
private fun VerticalDivider() {
    Box(
        modifier = Modifier
            .width(1.dp)
            .height(36.dp)
            .background(FoodCareWhite.copy(alpha = 0.3f))
    )
}

// ─── Componente: botão de ação ────────────────────────────────────────────
@Composable
private fun BotaoAcao(
    texto: String,
    icone: ImageVector,
    fundo: Color,
    texto_cor: Color,
    borda: Boolean = false,
    onClick: () -> Unit
) {
    val modifier = Modifier
        .fillMaxWidth()
        .height(52.dp)
        .clip(RoundedCornerShape(14.dp))
        .background(fundo)
        .then(
            if (borda) Modifier.border(1.5.dp, FoodCareRed.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
            else Modifier
        )
        .clickable { onClick() }

    Box(
        modifier         = modifier,
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment     = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector        = icone,
                contentDescription = null,
                tint               = texto_cor,
                modifier           = Modifier.size(20.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text       = texto,
                color      = texto_cor,
                fontWeight = FontWeight.SemiBold,
                fontSize   = 15.sp
            )
        }
    }
}

// ─── Componente: card de doação ───────────────────────────────────────────
@Composable
private fun DoacaoCard(doacao: DoacaoPendente) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        shape     = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        colors    = CardDefaults.cardColors(containerColor = FoodCareWhite)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment     = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Emoji / ícone do produto
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(FoodCareOffWhite),
                    contentAlignment = Alignment.Center
                ) {
                    Text(doacao.emoji, fontSize = 22.sp)
                }

                Spacer(Modifier.width(12.dp))

                Column {
                    Text(
                        text       = doacao.nomeProduto,
                        fontWeight = FontWeight.SemiBold,
                        fontSize   = 14.sp,
                        color      = FoodCareTextDark
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text     = "Para: ${doacao.receptor}",
                        fontSize = 12.sp,
                        color    = FoodCareSubText
                    )
                }
            }

            // Badge de status
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(doacao.statusColor.copy(alpha = 0.12f))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text       = doacao.status,
                    fontSize   = 10.sp,
                    color      = doacao.statusColor,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

// ─── Preview ───────────────────────────────────────────────────────────────
@Preview(showBackground = true, showSystemUi = true, name = "TelaDoador")
@Composable
fun TelaDoadorPreview() {
    MaterialTheme { TelaDoador() }
}

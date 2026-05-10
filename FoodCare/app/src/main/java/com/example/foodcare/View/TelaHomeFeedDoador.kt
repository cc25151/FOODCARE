package com.example.foodcare.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ─── Paleta ───────────────────────────────────────────────────────────────────
private val DHRed       = Color(0xFFD32F2F)
private val DHRedDark   = Color(0xFFAA1515)
private val DHRedLight  = Color(0xFFE53935)
private val DHWhite     = Color(0xFFFFFFFF)
private val DHOffWhite  = Color(0xFFF5F5F5)
private val DHGray      = Color(0xFFEEEEEE)
private val DHTextDark  = Color(0xFF1C1C1C)
private val DHSubText   = Color(0xFF757575)
private val DHAmber     = Color(0xFFFF8F00)
private val DHAmberBg   = Color(0xFFFFF8E1)

// ─────────────────────────────────────────────────────────────────────────────
//  TelaHomeFeedDoador
//
//  Parâmetros vindos do ViewModel / API:
//    nomeDoador   → nome do usuário doador  (tabela Usuario.nome)
//    doacoes      → lista de DoacaoPendenteUi montada pelo ViewModel
//                   cruzando Doacao + Alimento + Categoria
//
//  TODO: substituir List<DoacaoPendenteUi>() por fluxo real do ViewModel
// ─────────────────────────────────────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaHomeFeedDoador(
    // Dados vindos da API
    nomeDoador: String                  = "",
    doacoes: List<DoacaoPendenteUi>     = emptyList(),

    // Callbacks de navegação
    onPerfilClick: () -> Unit           = {},
    onRegistrarNovaDoacao: () -> Unit   = {}
) {
    Scaffold(
        containerColor = DHOffWhite,
        topBar = {
            Surface(
                color = DHWhite,
                shadowElevation = 3.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Logo + saudação
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(DHRed.copy(alpha = 0.08f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("🤝", fontSize = 22.sp)
                        }
                        Spacer(Modifier.width(10.dp))
                        Column {
                            Text(
                                "FoodCare",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 20.sp,
                                color = DHTextDark,
                                letterSpacing = (-0.3).sp
                            )
                            Text(
                                "Olá, $nomeDoador 👋",
                                fontSize = 11.sp,
                                color = DHSubText
                            )
                        }
                    }

                    // Avatar de perfil
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(DHOffWhite)
                            .clickable { onPerfilClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = "Perfil",
                            tint = DHTextDark,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {

            // ── Banner principal ──────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .shadow(6.dp, RoundedCornerShape(24.dp))
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        Brush.horizontalGradient(listOf(DHRedLight, DHRedDark))
                    )
                    .padding(24.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "Bem-vindo,",
                            color = DHWhite.copy(alpha = 0.8f),
                            fontSize = 14.sp
                        )
                        Text(
                            nomeDoador.ifBlank { "Doador" },
                            color = DHWhite,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 22.sp
                        )
                        Spacer(Modifier.height(12.dp))
                        Text(
                            "Sua generosidade faz a diferença\nna vida de muitas pessoas. 💚",
                            color = DHWhite.copy(alpha = 0.85f),
                            fontSize = 12.sp,
                            lineHeight = 18.sp
                        )
                    }
                    Spacer(Modifier.width(12.dp))
                    FoodCareIllustration(modifier = Modifier.size(100.dp))
                }
            }

            // ── Botão registrar nova doação ───────────────────────────────────
            Button(
                onClick = onRegistrarNovaDoacao,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = DHRed,
                    contentColor = DHWhite
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 3.dp)
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    "Registrar nova doação",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }

            Spacer(Modifier.height(24.dp))

            // ── Seção doações pendentes ───────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(DHAmber)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "Doações Pendentes",
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = DHTextDark
                    )
                }
                // Contador de pendências
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = DHAmberBg
                ) {
                    Text(
                        text = "${doacoes.size}",
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = DHAmber
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            // Cards de doações pendentes
            // Os campos são preenchidos pela API — exibidos vazios até lá
            if (doacoes.isEmpty()) {
                // Estado vazio — mostrado enquanto a lista não chega ou está vazia
                CardDoacaoVazio()
                Spacer(Modifier.height(10.dp))
                CardDoacaoVazio()
            } else {
                doacoes.forEach { doacao ->
                    CardDoacaoPendente(doacao = doacao)
                    Spacer(Modifier.height(10.dp))
                }
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  DoacaoPendenteUi
//  Objeto de UI que o ViewModel monta a partir do JOIN entre:
//    FoodCare.Doacao  → dataDoacao, horarioInicial, horarioFinal
//    FoodCare.Alimento → nome, descricao, qntd, validade
//    FoodCare.Categoria → nome (da categoria)
//
//  Todos os campos são String para facilitar a exibição direta.
//  Conversão/formatação deve ser feita no ViewModel.
// ─────────────────────────────────────────────────────────────────────────────
data class DoacaoPendenteUi(
    // Alimento
    val nomeAlimento: String,
    val descricao: String,
    val quantidade: String,
    val validade: String,
    val categoria: String,

    // Doacao
    val dataDoacao: String,
    val horarioInicial: String,
    val horarioFinal: String
)

// ─── Card com dados reais (vindos da API) ─────────────────────────────────────
@Composable
private fun CardDoacaoPendente(doacao: DoacaoPendenteUi) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = DHWhite),
        elevation = CardDefaults.cardElevation(3.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // Cabeçalho: nome + badge pendente
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(DHRed.copy(alpha = 0.10f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Fastfood,
                            null,
                            tint = DHRed,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(Modifier.width(10.dp))
                    Column {
                        Text(
                            doacao.nomeAlimento,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = DHTextDark
                        )
                        Text(
                            doacao.categoria,
                            fontSize = 11.sp,
                            color = DHSubText
                        )
                    }
                }
                BadgePendente()
            }

            Spacer(Modifier.height(14.dp))
            HorizontalDivider(color = DHGray)
            Spacer(Modifier.height(12.dp))

            // Grade de informações
            Row(Modifier.fillMaxWidth()) {
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    CampoCard(Icons.Default.Inventory2, "Quantidade", doacao.quantidade)
                    CampoCard(Icons.Default.Today,      "Data",       doacao.dataDoacao)
                    CampoCard(Icons.Default.Schedule,   "Horário",
                        "${doacao.horarioInicial} – ${doacao.horarioFinal}")
                }
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    CampoCard(Icons.Default.EventBusy,  "Validade",   doacao.validade)
                    CampoCard(Icons.Default.Description, "Descrição",  doacao.descricao)
                }
            }
        }
    }
}

// ─── Card esqueleto (estado vazio / carregando) ───────────────────────────────
@Composable
private fun CardDoacaoVazio() {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = DHWhite),
        elevation = CardDefaults.cardElevation(3.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .border(1.5.dp, DHAmber.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(DHRed.copy(alpha = 0.08f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Fastfood, null,
                            tint = DHRed.copy(alpha = 0.5f),
                            modifier = Modifier.size(20.dp))
                    }
                    Spacer(Modifier.width(10.dp))
                    Column {
                        // Barras de placeholder
                        Box(
                            Modifier
                                .width(120.dp).height(12.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(DHGray)
                        )
                        Spacer(Modifier.height(4.dp))
                        Box(
                            Modifier
                                .width(70.dp).height(9.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(DHGray)
                        )
                    }
                }
                BadgePendente()
            }

            Spacer(Modifier.height(14.dp))
            HorizontalDivider(color = DHGray)
            Spacer(Modifier.height(12.dp))

            // Placeholder das linhas de campos
            Row(Modifier.fillMaxWidth()) {
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    repeat(3) {
                        Box(
                            Modifier
                                .fillMaxWidth(0.85f).height(10.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(DHGray)
                        )
                    }
                }
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    repeat(2) {
                        Box(
                            Modifier
                                .fillMaxWidth(0.85f).height(10.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(DHGray)
                        )
                    }
                }
            }
        }
    }
}

// ─── Composables auxiliares ───────────────────────────────────────────────────

@Composable
private fun BadgePendente() {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = DHAmberBg
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box(
                Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(DHAmber)
            )
            Text("Pendente", fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold, color = DHAmber)
        }
    }
}

@Composable
private fun CampoCard(icone: ImageVector, label: String, valor: String) {
    Row(verticalAlignment = Alignment.Top) {
        Icon(icone, null, tint = DHSubText,
            modifier = Modifier.size(14.dp).padding(top = 1.dp))
        Spacer(Modifier.width(5.dp))
        Column {
            Text(label, fontSize = 10.sp, color = DHSubText)
            Text(
                valor.ifBlank { "—" },
                fontSize = 12.sp,
                color = DHTextDark,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
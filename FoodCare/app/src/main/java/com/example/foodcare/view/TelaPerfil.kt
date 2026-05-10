package com.example.foodcare.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ─── Paleta ───────────────────────────────────────────────────────────────────
private val PVermelho    = Color(0xFFD32F2F)
private val PVermelhoEsc = Color(0xFFAA1515)
private val PVermelhoClr = Color(0xFFE53935)
private val PBranco      = Color(0xFFFFFFFF)
private val PFundo       = Color(0xFFF5F5F5)
private val PTexto       = Color(0xFF1C1C1C)
private val PSubTexto    = Color(0xFF757575)
private val PDivisor     = Color(0xFFEEEEEE)
private val PVerde       = Color(0xFF2E7D32)
private val PVerdeFundo  = Color(0xFFE8F5E9)
private val PAzul        = Color(0xFF1565C0)
private val PAzulFundo   = Color(0xFFE3F2FD)

// ─────────────────────────────────────────────────────────────────────────────
//  TelaPerfil
//
//  Todos os parâmetros de dados são recebidos prontos do ViewModel/API.
//  Os valores padrão vazios garantem compilação; substitua pelo estado real.
//
//  Campos da tabela Usuario:
//    nome, email, documento (mascarado pelo backend), tipoPessoa ("PF"/"PJ"),
//    cidade, bairro, rua, numero, cep
//  Campo da tabela Doador:
//    pontuacao (Double? — null quando o usuário for Receptor)
//  Campo derivado:
//    tipoUsuario ("DOADOR" | "RECEPTOR")
// ─────────────────────────────────────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaPerfil(
    nome: String        = "",
    email: String       = "",
    documento: String   = "",   // CPF ou CNPJ mascarado
    tipoPessoa: String  = "",   // "PF" ou "PJ"
    tipoUsuario: String = "",   // "DOADOR" ou "RECEPTOR"
    cidade: String      = "",
    bairro: String      = "",
    rua: String         = "",
    numero: String      = "",
    cep: String         = "",
    pontuacao: Double?  = null, // Apenas Doadores possuem

    onVoltar: () -> Unit       = {},
    onEditarPerfil: () -> Unit = {},
    onLogout: () -> Unit       = {}
) {
    val isDoador      = tipoUsuario == "DOADOR"
    val tipoLabel     = if (isDoador) "Doador" else "Receptor"
    val tipoEmoji     = if (isDoador) "🤝" else "🙏"
    val tipoColor     = if (isDoador) PVerde else PAzul
    val tipoBg        = if (isDoador) PVerdeFundo else PAzulFundo
    val tipoIcone     = if (isDoador) Icons.Default.VolunteerActivism
    else          Icons.Default.CardGiftcard
    val tipoMensagem  = if (isDoador) "Obrigado por contribuir com a comunidade! 💚"
    else          "Conectado com doadores próximos a você 💙"
    val docLabel      = if (tipoPessoa == "PJ") "CNPJ" else "CPF"

    Scaffold(
        containerColor = PFundo,
        topBar = {
            TopAppBar(
                title = {
                    Text("Meu Perfil", fontWeight = FontWeight.Bold,
                        fontSize = 18.sp, color = PBranco)
                },
                navigationIcon = {
                    IconButton(onClick = onVoltar) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar", tint = PBranco)
                    }
                },
                actions = {
                    IconButton(onClick = onEditarPerfil) {
                        Icon(Icons.Default.Edit, contentDescription = "Editar", tint = PBranco)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PVermelho)
            )
        }
    ) { pad ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(pad)
                .verticalScroll(rememberScrollState())
        ) {

            // ── Hero ──────────────────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(listOf(PVermelhoClr, PVermelhoEsc))
                    )
                    .padding(vertical = 32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    // Avatar com inicial
                    Box(
                        modifier = Modifier
                            .size(88.dp)
                            .clip(CircleShape)
                            .background(PBranco.copy(alpha = 0.2f))
                            .border(3.dp, PBranco, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = nome.take(1).uppercase(),
                            fontSize = 36.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = PBranco
                        )
                    }

                    Spacer(Modifier.height(12.dp))
                    Text(nome, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = PBranco)
                    Spacer(Modifier.height(8.dp))

                    // Badge tipo usuário
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = PBranco.copy(alpha = 0.2f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(tipoEmoji, fontSize = 14.sp)
                            Text(tipoLabel, fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold, color = PBranco)
                            Text("·", color = PBranco.copy(alpha = 0.6f))
                            Text(
                                if (tipoPessoa == "PJ") "Pessoa Jurídica" else "Pessoa Física",
                                fontSize = 13.sp, color = PBranco.copy(alpha = 0.85f)
                            )
                        }
                    }

                    // Estrelas — visíveis somente para doadores
                    if (isDoador) {
                        Spacer(Modifier.height(12.dp))
                        EstrelasInline(pontuacao = pontuacao, corTexto = PBranco)
                    }
                }
            }

            // ── Cards ─────────────────────────────────────────────────────────
            Column(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {

                // Banner de tipo de usuário
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = tipoBg,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(tipoIcone, null, tint = tipoColor,
                            modifier = Modifier.size(26.dp))
                        Column {
                            Text("Você é um $tipoLabel",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp, color = tipoColor)
                            Text(tipoMensagem,
                                fontSize = 12.sp,
                                color = tipoColor.copy(alpha = 0.75f))
                        }
                    }
                }

                // Dados pessoais
                CardSecao("Dados Pessoais", Icons.Default.Person) {
                    LinhaDetalhe(Icons.Default.Badge,         "Nome",     nome)
                    LinhaDetalhe(Icons.Default.Email,         "E-mail",   email)
                    LinhaDetalhe(Icons.Default.AssignmentInd, docLabel,   documento)
                }

                // Endereço
                CardSecao("Endereço", Icons.Default.LocationOn) {
                    LinhaDetalhe(Icons.Default.Home,
                        "Logradouro", "$rua, $numero".trimEnd(',', ' '))
                    LinhaDetalhe(Icons.Default.Map,              "Bairro",  bairro)
                    LinhaDetalhe(Icons.Default.LocationCity,     "Cidade",  cidade)
                    LinhaDetalhe(Icons.Default.MarkunreadMailbox,"CEP",     cep)
                }

                // Desempenho — somente doadores
                if (isDoador) {
                    CardSecao("Meu Desempenho", Icons.Default.Star) {
                        CardNota(pontuacao)
                    }
                }

                Spacer(Modifier.height(4.dp))

                OutlinedButton(
                    onClick = onEditarPerfil,
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, PVermelho),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = PVermelho)
                ) {
                    Icon(Icons.Default.Edit, null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Editar Perfil", fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                }

                Button(
                    onClick = onLogout,
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFF0F0),
                        contentColor   = PVermelho
                    ),
                    elevation = ButtonDefaults.buttonElevation(0.dp)
                ) {
                    Icon(Icons.AutoMirrored.Filled.Logout, null,
                        modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Sair da conta", fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                }

                Spacer(Modifier.height(16.dp))
            }
        }
    }
}

// ─── Composables auxiliares compartilhados ────────────────────────────────────

@Composable
internal fun CardSecao(
    titulo: String,
    icone: ImageVector,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = Color(0xFFFFFFFF)),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier  = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 10.dp)
            ) {
                Icon(icone, null, tint = Color(0xFFD32F2F),
                    modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(8.dp))
                Text(titulo, fontWeight = FontWeight.Bold,
                    fontSize = 15.sp, color = Color(0xFF1C1C1C))
            }
            HorizontalDivider(color = Color(0xFFEEEEEE))
            Spacer(Modifier.height(10.dp))
            content()
        }
    }
}

@Composable
internal fun LinhaDetalhe(icone: ImageVector, label: String, valor: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icone, null, tint = Color(0xFF757575),
            modifier = Modifier.size(17.dp))
        Spacer(Modifier.width(10.dp))
        Column {
            Text(label, fontSize = 11.sp, color = Color(0xFF757575))
            Text(
                if (valor.isBlank()) "—" else valor,
                fontSize = 14.sp,
                color = Color(0xFF1C1C1C),
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
internal fun EstrelasInline(pontuacao: Double?, corTexto: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        val nota = pontuacao ?: 0.0
        repeat(5) { i ->
            Icon(
                if (i < nota.toInt()) Icons.Default.Star else Icons.Default.StarBorder,
                null, tint = Color(0xFFFFC107),
                modifier = Modifier.size(18.dp)
            )
        }
        Spacer(Modifier.width(4.dp))
        Text(
            if (pontuacao != null) String.format("%.1f", pontuacao) else "—",
            fontSize = 13.sp, color = corTexto, fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
internal fun CardNota(pontuacao: Double?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFFFFF8E1))
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text("Avaliação dos receptores",
                fontSize = 12.sp, color = Color(0xFF757575))
            Spacer(Modifier.height(6.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                val nota = pontuacao ?: 0.0
                repeat(5) { i ->
                    Icon(
                        if (i < nota.toInt()) Icons.Default.Star else Icons.Default.StarBorder,
                        null, tint = Color(0xFFFFA000),
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }
        Text(
            if (pontuacao != null) String.format("%.1f", pontuacao) else "—",
            fontSize = 32.sp, fontWeight = FontWeight.ExtraBold,
            color = Color(0xFFFFA000)
        )
    }
}
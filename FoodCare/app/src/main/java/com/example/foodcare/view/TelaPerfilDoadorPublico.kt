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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foodcare.ui.theme.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaPerfilDoadorPublico(
    nome: String       = "",
    tipoPessoa: String = "",
    cidade: String     = "",
    bairro: String     = "",
    pontuacao: Double? = null,

    onVoltar: () -> Unit = {}
) {
    Scaffold(
        containerColor = DFundo,
        topBar = {
            TopAppBar(
                title = {
                    Text("Perfil do Doador", fontWeight = FontWeight.Bold,
                        fontSize = 18.sp, color = DBranco)
                },
                navigationIcon = {
                    Surface(
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .clickable { onVoltar() },
                        color = DBranco.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, "Voltar",
                                tint = DBranco, modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("Voltar", color = DBranco, fontSize = 13.sp)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DVermelho)
            )
        }
    ) { pad ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(pad)
                .verticalScroll(rememberScrollState())
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(listOf(DVermelhoClr, DVermelhoEsc))
                    )
                    .padding(vertical = 32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    Box(
                        modifier = Modifier
                            .size(84.dp)
                            .clip(CircleShape)
                            .background(DBranco.copy(alpha = 0.2f))
                            .border(3.dp, DBranco, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = nome.take(1).uppercase(),
                            fontSize = 34.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = DBranco
                        )
                    }

                    Spacer(Modifier.height(12.dp))
                    Text(nome, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = DBranco)
                    Spacer(Modifier.height(8.dp))

                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = DBranco.copy(alpha = 0.2f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text("🤝", fontSize = 14.sp)
                            Text("Doador", fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold, color = DBranco)
                            Text("·", color = DBranco.copy(alpha = 0.6f))
                            Text(
                                if (tipoPessoa == "PJ") "Pessoa Jurídica" else "Pessoa Física",
                                fontSize = 13.sp, color = DBranco.copy(alpha = 0.85f)
                            )
                        }
                    }

                    Spacer(Modifier.height(14.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        val nota = pontuacao ?: 0.0
                        repeat(5) { i ->
                            Icon(
                                if (i < nota.toInt()) Icons.Default.Star
                                else Icons.Default.StarBorder,
                                null, tint = Color(0xFFFFC107),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(Modifier.width(6.dp))
                        Text(
                            if (pontuacao != null) String.format("%.1f", pontuacao)
                            else "Sem avaliação",
                            fontSize = 14.sp, color = DBranco,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                shape = RoundedCornerShape(10.dp),
                color = Color(0xFFFFF8E1)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Default.Lock, null,
                        tint = Color(0xFFF57F17),
                        modifier = Modifier.size(18.dp))
                    Text(
                        "Apenas informações públicas são exibidas neste perfil.",
                        fontSize = 12.sp, color = Color(0xFF6D4C00)
                    )
                }
            }

            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {

                CardSecao("Localização", Icons.Default.LocationOn) {
                    LinhaDetalhe(Icons.Default.LocationCity, "Cidade",
                        if (cidade.isBlank()) "—" else cidade)
                    LinhaDetalhe(Icons.Default.Map, "Bairro",
                        if (bairro.isBlank()) "—" else bairro)
                }

                CardSecao("Avaliação da Comunidade", Icons.Default.Star) {
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
                            Text("Nota dos receptores",
                                fontSize = 12.sp, color = DSubTexto)
                            Spacer(Modifier.height(6.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                                val nota = pontuacao ?: 0.0
                                repeat(5) { i ->
                                    Icon(
                                        if (i < nota.toInt()) Icons.Default.Star
                                        else Icons.Default.StarBorder,
                                        null, tint = DAmbar,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                            }
                        }
                        Text(
                            if (pontuacao != null) String.format("%.1f", pontuacao) else "—",
                            fontSize = 32.sp, fontWeight = FontWeight.ExtraBold, color = DAmbar
                        )
                    }
                }

                CardSecao("Tipo de Cadastro", Icons.Default.Badge) {
                    LinhaDetalhe(Icons.Default.AssignmentInd, "Categoria",
                        if (tipoPessoa == "PJ") "Pessoa Jurídica" else "Pessoa Física")
                    LinhaDetalhe(Icons.Default.VolunteerActivism,
                        "Papel na plataforma", "Doador de Alimentos 🤝")
                }

                Spacer(Modifier.height(24.dp))
            }
        }
    }
}
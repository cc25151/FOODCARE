package com.example.foodcare.view

import android.annotation.SuppressLint
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.foodcare.model.FoodCareData
import com.example.foodcare.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun TelaProdutoRequisitado(
    produtoId: Int,
    onVoltar: () -> Unit = {}
) {
    val produto = FoodCareData.produtos.find { it.id == produtoId }
        ?: FoodCareData.produtos.first()

    var mapaExpandido by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    Surface(
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .clickable { onVoltar() },
                        color = FoodCareWhite,
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Voltar",
                                tint = FoodCareRed,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Voltar",
                                color = FoodCareRed,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = FoodCareRed)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(FoodCareOffWhite)
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            // ── Red hero header ───────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(FoodCareLightRed, FoodCareDarkRed)
                        )
                    )
                    .padding(horizontal = 24.dp, vertical = 36.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = FoodCareWhite,
                        modifier = Modifier.size(52.dp)
                    )
                    Text(
                        text = "Seu produto está\npronto para recepção!",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = FoodCareWhite,
                        textAlign = TextAlign.Center,
                        lineHeight = 34.sp
                    )
                }
            }

            // ── Content ───────────────────────────────────────────────────────
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {

                // ── Dados card ────────────────────────────────────────────────
                SectionLabel("Dados:")

                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = FoodCareWhite),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Product info
                        Column(modifier = Modifier.weight(1f)) {
                            DadosRow(label = "Produto:", value = produto.nome)
                            Spacer(modifier = Modifier.height(8.dp))
                            DadosRow(label = "Alimento doação por:", value = produto.doador)
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        // Product image placeholder
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    brush = Brush.radialGradient(
                                        colors = listOf(
                                            Color(produto.imageColor).copy(alpha = 0.6f),
                                            Color(produto.imageColor)
                                        )
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "🛒", fontSize = 32.sp)
                        }
                    }
                }

                // ── Localização com mapa expansível ───────────────────────────
                SectionLabel("Localização:")

                MapaExpandivel(
                    expandido = mapaExpandido,
                    onToggle = { mapaExpandido = !mapaExpandido }
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Info text
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = FoodCareRed.copy(alpha = 0.08f)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = FoodCareRed,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = produto.doador,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = FoodCareTextDark
                            )
                            Text(
                                text = "Distância: ${produto.distancia} · ${produto.validade}",
                                fontSize = 12.sp,
                                color = Color(0xFF888888)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                FoodCareButton(text = "Voltar ao início", onClick = onVoltar, outlined = true)

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

// ─── Helpers ──────────────────────────────────────────────────────────────────

@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = FoodCareTextDark
    )
}

@Composable
private fun DadosRow(label: String, value: String) {
    Column {
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color(0xFF888888),
            fontWeight = FontWeight.Medium
        )
        Text(
            text = value,
            fontSize = 14.sp,
            color = FoodCareTextDark,
            fontWeight = FontWeight.SemiBold
        )
    }
}

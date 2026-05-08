package com.example.foodcare.view

import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.foodcare.model.FoodCareData

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun TelaProduto(
    produtoId: Int,
    onVoltar: () -> Unit = {},
    onQueroEsteProduto: (Int) -> Unit = {}
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
                            Text(text = "Voltar", color = FoodCareRed, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = FoodCareRed)
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(FoodCareOffWhite)
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                FoodCareButton(
                    text = "Quero este produto",
                    onClick = { onQueroEsteProduto(produto.id) }
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(FoodCareOffWhite)
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            // ── Hero image ────────────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
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
                Text(text = "🛒", fontSize = 72.sp)
            }

            // ── Content ───────────────────────────────────────────────────────
            Column(modifier = Modifier.padding(20.dp)) {

                // Product name
                Text(
                    text = produto.nome,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = FoodCareTextDark
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Validity badge
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = FoodCareRed.copy(alpha = 0.1f)
                ) {
                    Text(
                        text = produto.validade,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        fontSize = 12.sp,
                        color = FoodCareRed,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))
                SectionDivider()
                Spacer(modifier = Modifier.height(16.dp))

                // ── Doadores próximos ─────────────────────────────────────────
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = FoodCareRed,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Doadores próximos:",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = FoodCareTextDark
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Donor info card
                DoadorCard(nome = produto.doador, distancia = produto.distancia)

                Spacer(modifier = Modifier.height(16.dp))

                // ── Mapa expansível ───────────────────────────────────────────
                MapaExpandivel(
                    expandido = mapaExpandido,
                    onToggle = { mapaExpandido = !mapaExpandido }
                )

                Spacer(modifier = Modifier.height(20.dp))
                SectionDivider()
                Spacer(modifier = Modifier.height(16.dp))

                // ── Descrição ─────────────────────────────────────────────────
                Text(
                    text = "Descrição do produto:",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = FoodCareTextDark
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = produto.descricao,
                    fontSize = 14.sp,
                    color = Color(0xFF555555),
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(80.dp)) // space for bottom bar
            }
        }
    }
}

// ─── Expandable map ───────────────────────────────────────────────────────────
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun MapaExpandivel(
    expandido: Boolean,
    onToggle: () -> Unit,
    latitude: Double = -22.9099,   // Campinas, SP
    longitude: Double = -47.0626
) {
    val mapaUrl = buildOpenStreetMapUrl(latitude, longitude, if (expandido) 14 else 13)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFEEEEEE))
            .animateContentSize(animationSpec = tween(300))
    ) {
        // Header / toggle button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onToggle() }
                .background(FoodCareRed)
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = FoodCareWhite,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (expandido) "Ocultar mapa" else "Ver no mapa",
                    color = FoodCareWhite,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Icon(
                imageVector = if (expandido) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                contentDescription = null,
                tint = FoodCareWhite
            )
        }

        // Map WebView
        val mapHeight = if (expandido) 340.dp else 160.dp
        AndroidView(
            factory = { context ->
                WebView(context).apply {
                    webViewClient = WebViewClient()
                    settings.javaScriptEnabled = true
                    settings.loadWithOverviewMode = true
                    settings.useWideViewPort = true
                    settings.domStorageEnabled = true

                    loadUrl(mapaUrl)
                }
            },
            update = { webView ->
                webView.loadUrl(mapaUrl)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(mapHeight)
        )
    }
}

private fun buildOpenStreetMapUrl(lat: Double, lon: Double, zoom: Int): String {
    return "https://www.openstreetmap.org/#map=$zoom/$lat/$lon"
}

// ─── Doador card ──────────────────────────────────────────────────────────────
@Composable
fun DoadorCard(nome: String, distancia: String) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = FoodCareWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar placeholder
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(22.dp))
                    .background(FoodCareRed.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "👤", fontSize = 22.sp)
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(text = nome, fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = FoodCareTextDark)
                Text(text = distancia, fontSize = 13.sp, color = Color(0xFF888888))
            }

            Spacer(modifier = Modifier.weight(1f))

            // Distance badge
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = FoodCareRed
            ) {
                Text(
                    text = distancia,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    fontSize = 12.sp,
                    color = FoodCareWhite,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun SectionDivider() {
    HorizontalDivider(color = Color(0xFFEEEEEE), thickness = 1.dp)
}

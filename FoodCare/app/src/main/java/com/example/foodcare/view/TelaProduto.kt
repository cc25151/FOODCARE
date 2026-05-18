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
import com.example.foodcare.ui.theme.*
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

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
                        color = Branco,
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Voltar",
                                tint = Vermelho,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "Voltar", color = Vermelho, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Vermelho)
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BrancoAlt)
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
                .background(BrancoAlt)
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {

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

            Column(modifier = Modifier.padding(20.dp)) {

                Text(
                    text = produto.nome,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = textoEscuro
                )

                Spacer(modifier = Modifier.height(4.dp))


                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Vermelho.copy(alpha = 0.1f)
                ) {
                    Text(
                        text = produto.validade,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        fontSize = 12.sp,
                        color = Vermelho,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))
                SectionDivider()
                Spacer(modifier = Modifier.height(16.dp))


                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = Vermelho,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Doadores próximos:",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = textoEscuro
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                DoadorCard(nome = produto.doador, distancia = produto.distancia)

                Spacer(modifier = Modifier.height(16.dp))


                MapaSimples()

                Spacer(modifier = Modifier.height(20.dp))
                SectionDivider()
                Spacer(modifier = Modifier.height(16.dp))


                Text(
                    text = "Descrição do produto:",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = textoEscuro
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = produto.descricao,
                    fontSize = 14.sp,
                    color = Color(0xFF555555),
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}



@Composable
fun MapaSimples() {

    val posicao = LatLng(-22.9099, -47.0626) // Campinas

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(posicao, 15f)
    }

    GoogleMap(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        cameraPositionState = cameraPositionState,
        uiSettings = MapUiSettings(
            zoomControlsEnabled = true,
        zoomGesturesEnabled = true,
        scrollGesturesEnabled = true,
        rotationGesturesEnabled = true,
        tiltGesturesEnabled = true
        )
    ) {
        Marker(
            state = MarkerState(position = posicao),
            title = "Minha localização"
        )
    }
}


@Composable
fun DoadorCard(nome: String, distancia: String) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Branco),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(22.dp))
                    .background(Vermelho.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "👤", fontSize = 22.sp)
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(text = nome, fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = textoEscuro)
                Text(text = distancia, fontSize = 13.sp, color = Color(0xFF888888))
            }

            Spacer(modifier = Modifier.weight(1f))


            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Vermelho
            ) {
                Text(
                    text = distancia,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    fontSize = 12.sp,
                    color = Branco,
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

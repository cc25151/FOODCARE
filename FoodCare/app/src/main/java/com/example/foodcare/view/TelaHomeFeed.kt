package com.example.foodcare.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foodcare.model.FoodCareData
import com.example.foodcare.model.Produto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaHomeFeed(
    onProdutoClick: (Int) -> Unit = {}
) {
    var busca by remember { mutableStateOf("") }
    var categoriaSelecionada by remember { mutableStateOf<String?>(null) }

    val produtosFiltrados = FoodCareData.produtos.filter { p ->
        val matchBusca = busca.isBlank() || p.nome.contains(busca, ignoreCase = true)
        val matchCat = categoriaSelecionada == null || p.categoria == categoriaSelecionada
        matchBusca && matchCat
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "FoodCare",
                        color = FoodCareWhite,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "Carrinho",
                            tint = FoodCareWhite
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = FoodCareRed
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(FoodCareOffWhite)
                .padding(innerPadding)
        ) {
            // ── Search bar ────────────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(FoodCareRed)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                OutlinedTextField(
                    value = busca,
                    onValueChange = { busca = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text("Pesquisar por Doação", color = Color(0xFFBBBBBB), fontSize = 14.sp)
                    },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = null, tint = Color(0xFF888888))
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        focusedContainerColor = FoodCareWhite,
                        unfocusedContainerColor = FoodCareWhite,
                        cursorColor = FoodCareRed
                    )
                )
            }

            // ── Section title ─────────────────────────────────────────────────
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Alimentos Sugeridos",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = FoodCareTextDark,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // ── Product grid ──────────────────────────────────────────────────
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(produtosFiltrados) { produto ->
                    ProdutoCard(produto = produto, onClick = { onProdutoClick(produto.id) })
                }
            }

            // ── Category row ──────────────────────────────────────────────────
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(FoodCareWhite)
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    CategoriaChip(
                        nome = "Todos",
                        selecionado = categoriaSelecionada == null,
                        onClick = { categoriaSelecionada = null }
                    )
                }
                items(FoodCareData.categorias) { cat ->
                    CategoriaChip(
                        nome = cat.nome,
                        selecionado = categoriaSelecionada == cat.nome,
                        onClick = {
                            categoriaSelecionada = if (categoriaSelecionada == cat.nome) null else cat.nome
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ProdutoCard(produto: Produto, onClick: () -> Unit) {
    var favoritado by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .shadow(2.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = FoodCareWhite)
    ) {
        Column {
            // Image placeholder with gradient
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color(produto.imageColor).copy(alpha = 0.7f),
                                Color(produto.imageColor)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "🍽️", fontSize = 36.sp)

                // Favorite icon
                IconButton(
                    onClick = { favoritado = !favoritado },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(4.dp)
                        .size(30.dp)
                        .background(Color.White.copy(alpha = 0.7f), CircleShape)
                ) {
                    Icon(
                        imageVector = if (favoritado) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favoritar",
                        tint = FoodCareRed,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Column(modifier = Modifier.padding(10.dp)) {
                Text(
                    text = produto.nome,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = FoodCareTextDark,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = produto.distancia,
                    fontSize = 11.sp,
                    color = Color(0xFF888888)
                )
            }
        }
    }
}

@Composable
fun CategoriaChip(nome: String, selecionado: Boolean, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        color = if (selecionado) FoodCareRed else FoodCareGray
    ) {
        Text(
            text = nome,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            fontSize = 13.sp,
            fontWeight = if (selecionado) FontWeight.SemiBold else FontWeight.Normal,
            color = if (selecionado) FoodCareWhite else FoodCareTextDark
        )
    }
}

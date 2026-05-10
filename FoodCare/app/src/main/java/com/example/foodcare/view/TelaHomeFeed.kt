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
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.util.copy
import com.example.foodcare.model.FoodCareData.produtos
import com.example.foodcare.model.FoodCareData.categoriasVisuais
import com.example.foodcare.model.Produto

// ─── Cores centralizadas ───────────────────────────────────────────────────
val FoodCareRed       = Color(0xFFD32F2F)
val FoodCareWhite     = Color(0xFFFFFFFF)
val FoodCareOffWhite  = Color(0xFFF5F5F5)
val FoodCareTextDark  = Color(0xFF1C1C1C)
val FoodCareSubText   = Color(0xFF757575)
val FoodCareDivider   = Color(0xFFEEEEEE)




private fun emojiParaProduto(categoria: String): String = when {
    categoria.contains("Marmita", ignoreCase = true) -> "🍱"
    categoria.contains("Grão",    ignoreCase = true) -> "🌾"
    categoria.contains("Vegetal", ignoreCase = true) -> "🥦"
    categoria.contains("Padaria", ignoreCase = true) -> "🍞"
    else -> "🛒"
}

// ─── Tela principal ────────────────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaHomeFeed(
    nomeUsuario: String = "Receptor",
    onProdutoClick: (Int) -> Unit = {},
    onPerfilClick: () -> Unit = {}
) {
    var busca by remember { mutableStateOf("") }
    var categoriaSelecionada by remember { mutableStateOf<String?>(null) }

    val produtosFiltrados = remember(busca, categoriaSelecionada) {
        produtos.filter { p ->
            (busca.isBlank() || p.nome.contains(busca, ignoreCase = true)) &&
                    (categoriaSelecionada == null ||
                            p.categoria.contains(categoriaSelecionada!!, ignoreCase = true))
        }
    }

    Scaffold(
        containerColor = FoodCareOffWhite,
        // ── Cabeçalho branco (estilo imagem 1) ──────────────────────────
        topBar = {
            Surface(
                color = FoodCareWhite,
                shadowElevation = 3.dp,
                tonalElevation  = 0.dp
            ) {
                Column {
                    // Linha superior: logo + perfil
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .statusBarsPadding()
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Logo
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // Ícone da marca
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(FoodCareRed.copy(alpha = 0.08f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("🛍️", fontSize = 24.sp)
                            }
                            Spacer(Modifier.width(10.dp))
                            Column {
                                Text(
                                    text       = "FoodCare",
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize   = 20.sp,
                                    color      = FoodCareTextDark,
                                    letterSpacing = (-0.3).sp
                                )
                                Text(
                                    text     = "Olá, $nomeUsuario 👋",
                                    fontSize = 11.sp,
                                    color    = FoodCareSubText
                                )
                            }
                        }

                        // Ícone de perfil
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(FoodCareOffWhite)
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

                    // ── Barra de pesquisa ──────────────────────────────
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .padding(bottom = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value         = busca,
                            onValueChange = { busca = it },
                            modifier      = Modifier.weight(1f),
                            placeholder   = {
                                Text(
                                    "Pesquisar por Alimentos",
                                    color    = Color(0xFFBBBBBB),
                                    fontSize = 13.sp
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.Search,
                                    contentDescription = null,
                                    tint     = Color(0xFF999999),
                                    modifier = Modifier.size(18.dp)
                                )
                            },
                            singleLine = true,
                            shape      = RoundedCornerShape(24.dp),
                            colors     = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor      = FoodCareRed.copy(alpha = 0.4f),
                                unfocusedBorderColor    = FoodCareDivider,
                                focusedContainerColor   = FoodCareWhite,
                                unfocusedContainerColor = FoodCareOffWhite,
                                cursorColor             = FoodCareRed
                            )
                        )

                        Spacer(Modifier.width(8.dp))

                        // Botão de filtro
                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(FoodCareWhite)
                                .shadow(1.dp, RoundedCornerShape(12.dp))
                                .clickable { },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector        = Icons.Default.Tune,
                                contentDescription = "Filtros",
                                tint               = FoodCareTextDark,
                                modifier           = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(FoodCareOffWhite)
        ) {

            // ── Chips de categorias ────────────────────────────────────
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(FoodCareWhite)
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Chip "Todos"
                item {
                    CategoriaChip(
                        nome       = "Todos",
                        emoji      = "🍽️",
                        selecionado = categoriaSelecionada == null,
                        onClick    = { categoriaSelecionada = null }
                    )
                }
                items(categoriasVisuais) { cat ->
                    CategoriaChip(
                        nome        = cat.nome,
                        emoji       = cat.emoji,
                        selecionado = categoriaSelecionada == cat.nome,
                        onClick     = {
                            categoriaSelecionada =
                                if (categoriaSelecionada == cat.nome) null else cat.nome
                        }
                    )
                }
            }

            Spacer(Modifier.height(4.dp))

            // ── Título da seção ────────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text       = "Alimentos Sugeridos",
                    fontWeight = FontWeight.Bold,
                    fontSize   = 17.sp,
                    color      = FoodCareTextDark
                )
                Text(
                    text     = "Ver todos",
                    fontSize = 12.sp,
                    color    = FoodCareRed,
                    fontWeight = FontWeight.Medium
                )
            }

            // ── Grid de produtos ───────────────────────────────────────
            if (produtosFiltrados.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("😕", fontSize = 40.sp)
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "Nenhum alimento encontrado",
                            color    = FoodCareSubText,
                            fontSize = 14.sp
                        )
                    }
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement   = Arrangement.spacedBy(10.dp)
                ) {
                    items(produtosFiltrados) { produto ->
                        AlimentoCard(
                            produto = produto,
                            onClick = { onProdutoClick(produto.id) }
                        )
                    }
                }
            }
        }
    }
}

// ─── Card de alimento (estilo imagem 1) ───────────────────────────────────
@Composable
private fun AlimentoCard(produto: Produto, onClick: () -> Unit) {
    var favoritado by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape  = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = FoodCareWhite)
    ) {
        Column {
            // ── Área de imagem / placeholder ──────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .background(
                        color = Color(produto.imageColor),
                        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                    )
            ) {
                // Emoji central representativo
                Text(
                    text     = emojiParaProduto(produto.categoria),
                    fontSize = 42.sp,
                    modifier = Modifier.align(Alignment.Center)
                )

                // Botão de favoritar (coração) — canto superior direito
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(FoodCareWhite.copy(alpha = 0.8f))
                        .clickable { favoritado = !favoritado },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (favoritado) Icons.Default.Favorite
                        else            Icons.Default.FavoriteBorder,
                        contentDescription = "Favoritar",
                        tint     = FoodCareRed,
                        modifier = Modifier.size(15.dp)
                    )
                }

                // Badge de distância — canto inferior esquerdo
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.Black.copy(alpha = 0.45f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text     = "📍 ${produto.distancia}",
                        fontSize = 9.sp,
                        color    = FoodCareWhite,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // ── Informações ───────────────────────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 8.dp)
            ) {
                Text(
                    text       = produto.nome,
                    fontSize   = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color      = FoodCareTextDark,
                    maxLines   = 2,
                    overflow   = TextOverflow.Ellipsis
                )

                Spacer(Modifier.height(2.dp))

                

                Spacer(Modifier.height(8.dp))

                // Linha inferior: disponibilidade + botão "+"
                Row(
                    modifier              = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment     = Alignment.CenterVertically
                ) {
                    Text(
                        text     = "🕐 ${produto.validade}",
                        fontSize = 10.sp,
                        color    = FoodCareSubText
                    )

                    Box(
                        modifier = Modifier
                            .size(26.dp)
                            .clip(CircleShape)
                            .background(FoodCareRed)
                            .clickable { onClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector        = Icons.Default.Add,
                            contentDescription = "Adicionar",
                            tint               = FoodCareWhite,
                            modifier           = Modifier.size(14.dp)
                        )
                    }
                }
            }
        }
    }
}

// ─── Chip de categoria ─────────────────────────────────────────────────────
@Composable
private fun CategoriaChip(
    nome: String,
    emoji: String,
    selecionado: Boolean,
    onClick: () -> Unit
) {
    val bg    = if (selecionado) FoodCareRed else FoodCareOffWhite
    val texto = if (selecionado) FoodCareWhite else FoodCareTextDark

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(bg)
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(emoji, fontSize = 13.sp)
            Spacer(Modifier.width(4.dp))
            Text(
                text       = nome,
                fontSize   = 12.sp,
                color      = texto,
                fontWeight = if (selecionado) FontWeight.SemiBold else FontWeight.Normal
            )
        }
    }
}
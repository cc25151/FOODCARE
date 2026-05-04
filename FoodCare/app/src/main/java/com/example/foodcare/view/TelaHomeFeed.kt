package com.example.foodcare.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foodcare.model.FoodCareData
import com.example.foodcare.model.Produto

private data class CategoriaVisual(val nome: String, val emoji: String)
private val categoriasVisuais = listOf(
    CategoriaVisual("Marmitas", "🍱"),
    CategoriaVisual("Alimentos não perecíveis", "🌾")
)

private val produtosHome = listOf(
    Produto(1, "Cesta Básica",      "", "Categoria A", "Mercado", "1km", "Hoje", 0xFFFFF3E0),
    Produto(2, "Marmita Média",     "", "Categoria B", "Rest.",   "1km", "Hoje", 0xFFFFEBEE),
    Produto(3, "Marmita Pequena",   "", "Marmitas",    "Rest.",   "1km", "Hoje", 0xFFFFF3E0),
    Produto(4, "Pacote de Arroz",   "", "Alimentos não perecíveis", "Mercado", "1km", "Hoje", 0xFFFFF8E1)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaHomeFeed(
    nomeUsuario: String = "Receptor",
    onProdutoClick: (Int) -> Unit = {},
    onPerfilClick: () -> Unit = {}
) {
    var busca by remember { mutableStateOf("") }

    Scaffold(
        containerColor = FoodCareOffWhite,
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(FoodCareWhite.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("🛒", fontSize = 22.sp)
                        }
                        Spacer(Modifier.width(10.dp))
                        Text(
                            text = "FoodCare",
                            color = FoodCareWhite,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    }
                },
                actions = {
                    Box(
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(FoodCareWhite.copy(alpha = 0.25f))
                            .clickable { onPerfilClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Perfil",
                            tint = FoodCareWhite,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = FoodCareRed)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(FoodCareOffWhite)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = busca,
                    onValueChange = { busca = it },
                    modifier = Modifier.weight(1f),
                    placeholder = {
                        Text(
                            "Pesquisar por Alimentos",
                            color = Color(0xFFBBBBBB),
                            fontSize = 13.sp
                        )
                    },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = null,
                            tint = Color(0xFF999999),
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor   = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        focusedContainerColor   = FoodCareWhite,
                        unfocusedContainerColor = FoodCareWhite,
                        cursorColor = FoodCareRed
                    )
                )

                Spacer(Modifier.width(8.dp))

                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(FoodCareWhite)
                        .clickable { },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Tune,
                        contentDescription = "Filtros",
                        tint = FoodCareTextDark,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Text(
                text = "Alimentos Sugeridos",
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                color = FoodCareTextDark,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )

            Spacer(Modifier.height(8.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement   = Arrangement.spacedBy(10.dp)
            ) {
                items(produtosHome) { produto ->
                    AlimentoCard(
                        produto   = produto,
                        onClick   = { onProdutoClick(produto.id) }
                    )
                }
            }

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(FoodCareWhite)
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(categoriasVisuais) { cat ->
                    CategoriaLabel(nome = cat.nome, emoji = cat.emoji)
                }
            }
        }
    }
}

@Composable
private fun AlimentoCard(produto: Produto, onClick: () -> Unit) {
    var favoritado by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .shadow(1.dp, RoundedCornerShape(16.dp)),
        shape  = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = FoodCareWhite)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .background(
                        color = Color(produto.imageColor),
                        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                    )
            ) {
                Text(
                    text     = "🛒",
                    fontSize = 36.sp,
                    modifier = Modifier.align(Alignment.Center)
                )

                IconButton(
                    onClick  = { favoritado = !favoritado },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(4.dp)
                        .size(28.dp)
                ) {
                    Icon(
                        imageVector = if (favoritado) Icons.Default.Favorite
                        else            Icons.Default.FavoriteBorder,
                        contentDescription = "Favoritar",
                        tint     = FoodCareRed,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 8.dp)
            ) {
                Text(
                    text     = produto.nome,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color    = FoodCareTextDark,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(Modifier.height(6.dp))

                Row(
                    modifier              = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
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

@Composable
private fun CategoriaLabel(nome: String, emoji: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = emoji, fontSize = 22.sp)
        Spacer(Modifier.height(2.dp))
        Text(
            text     = nome,
            fontSize = 10.sp,
            color    = FoodCareTextDark,
            fontWeight = FontWeight.Medium
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "TelaHomeFeed")
@Composable
fun TelaHomeFeedPreview() {
    MaterialTheme {
        TelaHomeFeed()
    }
}
package com.example.foodcare.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foodcare.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaQuemSomos(
    onVoltar: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {},

                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Vermelho
                )
            )
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
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(vermClaro, vermEscuro)
                        )
                    )
                    .padding(horizontal = 24.dp, vertical = 32.dp)
            ) {
                Column {
                    Text(
                        text = "FoodCare",
                        color = Branco,
                        fontSize = 14.sp,
                        letterSpacing = 2.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Quem somos nós?",
                        color = Branco,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                SectionCard(
                    title = "Quem somos nós?",
                    body = "A FoodCare conecta com uma plataforma digital que conecta doadores de alimentos a pessoas e instituições que necessitam, priorizando o reaproveitamento de alimentos de forma organizada e eficiente."
                )

                SectionCard(
                    title = "Nosso Objetivo",
                    body = "O objetivo do FoodCare é reduzir o desperdício de alimentos e facilitar sua redistribuição, utilizando recursos tecnológicos para priorizar doações com base em critérios como validade, proximidade e demanda. Dessa forma, busca se constituir pela segurança alimentar e gerar impacto social positivo."
                )

                Spacer(modifier = Modifier.height(8.dp))

                FoodCareButton(
                    text = "Voltar",
                    onClick = onVoltar,
                    outlined = true
                )
            }
        }
    }
}

@Composable
private fun SectionCard(title: String, body: String) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = textoEscuro
        )
        Text(
            text = body,
            fontSize = 14.sp,
            color = Color(0xFF555555),
            lineHeight = 22.sp
        )
    }
}

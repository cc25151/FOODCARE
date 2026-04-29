package com.example.foodcare.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaQuemSomos(
    onVoltar: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onVoltar) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar",
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
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(FoodCareLightRed, FoodCareDarkRed)
                        )
                    )
                    .padding(horizontal = 24.dp, vertical = 32.dp)
            ) {
                Column {
                    Text(
                        text = "FoodCare",
                        color = FoodCareWhite,
                        fontSize = 14.sp,
                        letterSpacing = 2.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Quem somos nós?",
                        color = FoodCareWhite,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Content
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
            color = FoodCareTextDark
        )
        Text(
            text = body,
            fontSize = 14.sp,
            color = Color(0xFF555555),
            lineHeight = 22.sp
        )
    }
}

package com.example.foodcare.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaCadastroReceptor(
    onEntrar: () -> Unit = {},
    onJaTenhoConta: () -> Unit = {},
    onVoltar: () -> Unit = {}
) {
    var nome by remember { mutableStateOf("") }
    var cpfCnpj by remember { mutableStateOf("") }
    var cep by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var telefone by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var senhaVisivel by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onVoltar) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar",
                            tint = FoodCareRed
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = FoodCareOffWhite
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(FoodCareOffWhite)
                .padding(innerPadding)
                .padding(horizontal = 28.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Cadastrar - Receptor",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = FoodCareTextDark
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Nome
            FieldLabel("Digite seu nome:")
            Spacer(modifier = Modifier.height(6.dp))
            FoodCareTextField(
                value = nome,
                onValueChange = { nome = it },
                placeholder = "Nome completo"
            )

            Spacer(modifier = Modifier.height(18.dp))

            // CPF/CNPJ
            FieldLabel("Digite seu CPF/CNPJ:")
            Spacer(modifier = Modifier.height(6.dp))
            FoodCareTextField(
                value = cpfCnpj,
                onValueChange = { cpfCnpj = it },
                placeholder = "000.000.000-00",
                keyboardType = KeyboardType.Number
            )

            Spacer(modifier = Modifier.height(18.dp))

            // CEP
            FieldLabel("Digite seu CEP")
            Spacer(modifier = Modifier.height(6.dp))
            FoodCareTextField(
                value = cep,
                onValueChange = { cep = it },
                placeholder = "00000-000",
                keyboardType = KeyboardType.Number
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Email
            FieldLabel("Digite seu email:")
            Spacer(modifier = Modifier.height(6.dp))
            FoodCareTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = "seu@email.com",
                keyboardType = KeyboardType.Email
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Telefone
            FieldLabel("Digite seu telefone")
            Spacer(modifier = Modifier.height(6.dp))
            FoodCareTextField(
                value = telefone,
                onValueChange = { telefone = it },
                placeholder = "(00) 00000-0000",
                keyboardType = KeyboardType.Phone
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Senha
            FieldLabel("Digite sua senha:")
            Spacer(modifier = Modifier.height(6.dp))
            FoodCareTextField(
                value = senha,
                onValueChange = { senha = it },
                placeholder = "••••••••",
                keyboardType = KeyboardType.Password,
                visualTransformation = if (senhaVisivel) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { senhaVisivel = !senhaVisivel }) {
                        Icon(
                            imageVector = if (senhaVisivel) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = null,
                            tint = Color(0xFF888888)
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(32.dp))

            FoodCareButton(text = "Entrar", onClick = onEntrar)

            Spacer(modifier = Modifier.height(16.dp))

            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                TextButton(onClick = onJaTenhoConta) {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(SpanStyle(color = FoodCareTextDark)) {
                                append("Já tem uma conta? ")
                            }
                            withStyle(SpanStyle(color = FoodCareRed, fontWeight = FontWeight.Bold)) {
                                append("Entre agora")
                            }
                        },
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

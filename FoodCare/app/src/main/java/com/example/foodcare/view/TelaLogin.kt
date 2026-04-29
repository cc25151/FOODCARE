package com.example.foodcare.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaLogin(
    onLoginClick: () -> Unit = {},
    onGoogleLogin: () -> Unit = {},
    onCriarConta: () -> Unit = {},
    onVoltar: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
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
                text = "Entrar",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = FoodCareTextDark
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Email
            FieldLabel("Digite seu email:")
            Spacer(modifier = Modifier.height(6.dp))
            FoodCareTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = "seu@email.com",
                keyboardType = KeyboardType.Email
            )

            Spacer(modifier = Modifier.height(20.dp))

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

            FoodCareButton(text = "Login", onClick = onLoginClick)

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0xFFDDDDDD))
                Text(
                    text = "  Ou  ",
                    fontSize = 13.sp,
                    color = Color(0xFF888888)
                )
                HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0xFFDDDDDD))
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Google button
            OutlinedButton(
                onClick = onGoogleLogin,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, FoodCareRed),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = FoodCareRed)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Entrar com o Google",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = FoodCareRed
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    // Simple "G" letter as placeholder for Google icon
                    Text(
                        text = "G",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = FoodCareRed
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Create account link
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                TextButton(onClick = onCriarConta) {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(SpanStyle(color = FoodCareTextDark)) {
                                append("Não tem uma conta? ")
                            }
                            withStyle(SpanStyle(color = FoodCareRed, fontWeight = FontWeight.Bold)) {
                                append("Crie Uma")
                            }
                        },
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            FoodCareButton(text = "Voltar", onClick = onVoltar, outlined = true)

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

// ─── Shared composables ───────────────────────────────────────────────────────

@Composable
fun FieldLabel(text: String) {
    Text(
        text = text,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        color = FoodCareTextDark
    )
}

@Composable
fun FoodCareTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "",
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(text = placeholder, color = Color(0xFFBBBBBB), fontSize = 14.sp) },
        singleLine = true,
        shape = RoundedCornerShape(10.dp),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        visualTransformation = visualTransformation,
        trailingIcon = trailingIcon,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = FoodCareRed,
            unfocusedBorderColor = Color(0xFFDDDDDD),
            focusedLabelColor = FoodCareRed,
            cursorColor = FoodCareRed,
            focusedContainerColor = FoodCareWhite,
            unfocusedContainerColor = FoodCareWhite
        )
    )
}

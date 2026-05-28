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
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.foodcare.ui.theme.*
import com.example.foodcare.viewmodel.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaLogin(
    onLoginClick: (String) -> Unit = {},
    onCriarConta: () -> Unit = {},
    onVoltar: () -> Unit = {},
    viewModel: LoginViewModel = viewModel()
) {
    LaunchedEffect(viewModel.loginSucesso) {
        if (viewModel.loginSucesso) {
            onLoginClick(viewModel.qualTipoUsuario)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onVoltar) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar",
                            tint = Vermelho
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BrancoAlt
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BrancoAlt)
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
                color = textoEscuro
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Email
            FieldLabel("Digite seu email:")
            Spacer(modifier = Modifier.height(6.dp))
            FoodCareTextField(
                value = viewModel.email,
                onValueChange = { viewModel.email = it },
                placeholder = "seu@email.com",
                keyboardType = KeyboardType.Email
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Senha
            FieldLabel("Digite sua senha:")
            Spacer(modifier = Modifier.height(6.dp))
            FoodCareTextField(
                value = viewModel.senha,
                onValueChange = { viewModel.senha = it },
                placeholder = "••••••••",
                keyboardType = KeyboardType.Password,
                visualTransformation = if (viewModel.senhaVisivel) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { viewModel.senhaVisivel = !viewModel.senhaVisivel }) {
                        Icon(
                            imageVector = if (viewModel.senhaVisivel) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = null,
                            tint = Color(0xFF888888)
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(32.dp))

            Column{
                Text(text = viewModel.mensagemErro,
                    color = Color.Red,
                    fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(4.dp))

            FoodCareButton(text = "Login", onClick = {viewModel.FazerLogin()})

            LaunchedEffect(viewModel.loginSucesso){

                if(viewModel.loginSucesso){
                    onLoginClick(viewModel.qualTipoUsuario)
                }
            }


            Spacer(modifier = Modifier.height(24.dp))

            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                TextButton(onClick = onCriarConta) {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(SpanStyle(color = textoEscuro)) {
                                append("Não tem uma conta? ")
                            }
                            withStyle(SpanStyle(color = Vermelho, fontWeight = FontWeight.Bold)) {
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


@Composable
fun FieldLabel(text: String) {
    Text(
        text = text,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        color = textoEscuro
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
            focusedBorderColor = Vermelho,
            unfocusedBorderColor = Color(0xFFDDDDDD),
            focusedLabelColor = Vermelho,
            cursorColor = Vermelho,
            focusedContainerColor = Branco,
            unfocusedContainerColor = Branco
        )
    )
}

package com.example.foodcare.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
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
import com.example.foodcare.ui.theme.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.foodcare.viewmodel.CadastroViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaCadastroReceptor(
    onEntrar: () -> Unit = {},
    onJaTenhoConta: () -> Unit = {},
    onVoltar: () -> Unit = {},
    viewModel: CadastroViewModel = viewModel()
) {
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
                text = "Cadastrar",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = textoEscuro,
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Nome
            FieldLabel("Digite seu nome:")
            Spacer(modifier = Modifier.height(6.dp))
            FoodCareTextField(
                value = viewModel.nome,
                onValueChange = { viewModel.nome = it },
                placeholder = "Nome completo"
            )

            Spacer(modifier = Modifier.height(18.dp))

            //CPF / CNPJ
            FieldLabel("Você é:")
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (viewModel.tipoPessoa == "PF"),
                    onClick = { viewModel.tipoPessoa = "PF" },
                    colors = RadioButtonDefaults.colors(selectedColor = Vermelho)
                )
                Text(text = "Pessoa Física", color = textoEscuro, fontSize = 16.sp)

                RadioButton(
                    selected = (viewModel.tipoPessoa == "PJ"),
                    onClick = { viewModel.tipoPessoa = "PJ" },
                    colors = RadioButtonDefaults.colors(selectedColor = Vermelho)
                )
                Text(text = "Instituição / Empresa", color = textoEscuro, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(18.dp))

            // CPF/CNPJ
            FieldLabel("Digite seu CPF/CNPJ:")
            Spacer(modifier = Modifier.height(6.dp))
            FoodCareTextField(
                value = viewModel.documento,
                onValueChange = { viewModel.documento = it },
                placeholder = "000.000.000-00",
                keyboardType = KeyboardType.Number
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Email
            FieldLabel("Digite seu email:")
            Spacer(modifier = Modifier.height(6.dp))
            FoodCareTextField(
                value = viewModel.email,
                onValueChange = { viewModel.email = it },
                placeholder = "seu@email.com",
                keyboardType = KeyboardType.Email
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Senha
            FieldLabel("Digite sua senha:")
            Spacer(modifier = Modifier.height(6.dp))
            FoodCareTextField(
                value = viewModel.senha,
                onValueChange = { viewModel.senha = it },
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

            if (viewModel.mensagemErro.isNotEmpty()) {
                Text(
                    text = viewModel.mensagemErro,
                    color = Color.Red,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }

            FoodCareButton(text = "Entrar", onClick = onEntrar)

            //Acrescentar LaunchedEffect

            Spacer(modifier = Modifier.height(16.dp))

            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                TextButton(onClick = onJaTenhoConta) {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(SpanStyle(color = textoEscuro)) {
                                append("Já tem uma conta? ")
                            }
                            withStyle(SpanStyle(color = Vermelho, fontWeight = FontWeight.Bold)) {
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
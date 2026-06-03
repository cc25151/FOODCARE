package com.example.foodcare.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foodcare.ui.theme.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.foodcare.viewmodel.CadastroAlimentoViewModel
import java.text.SimpleDateFormat
import java.util.*
import com.example.foodcare.model.FoodCareData
import android.widget.Toast
import com.example.foodcare.model.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaCadastrarAlimento(
    onVoltar: () -> Unit                  = {},
    onProximo: (AlimentoFormData) -> Unit = {},
    viewModel: CadastroAlimentoViewModel = viewModel()
) {
    val context = LocalContext.current
    var mostrarCalendario by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    var categoriaExpandida by remember { mutableStateOf(false) }

    val camposValidos = viewModel.nome.isNotBlank() &&
            viewModel.quantidade.isNotBlank() &&
            viewModel.validade.isNotBlank() &&
            viewModel.categoriaSelecionada != null

    LaunchedEffect(viewModel.cadastroSucesso) {
        if (viewModel.cadastroSucesso) {
            Toast.makeText(context, "Alimento cadastrado!", Toast.LENGTH_SHORT).show()
            viewModel.resetarFormulario()
        }
    }


    Scaffold(
        containerColor = CAFundo,
        topBar = {
            TopAppBar(
                title = {
                    Text("Nova Doação", fontWeight = FontWeight.Bold,
                        fontSize = 18.sp, color = CABranco)
                },
                navigationIcon = {
                    IconButton(onClick = onVoltar) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack,
                            "Voltar", tint = CABranco)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = CAVermelho)
            )
        }
    ) { pad ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(pad)
                .verticalScroll(rememberScrollState())
        ) {


            IndicadorPassos(passoAtual = 1, totalPassos = 2)


            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(listOf(CAVermelhoC, CAVermelhoE))
                    )
                    .padding(horizontal = 20.dp, vertical = 20.dp)
            ) {
                Column {
                    Text(
                        "Passo 1 de 2", color = CABranco.copy(alpha = 0.75f),
                        fontSize = 12.sp
                    )
                    Text(
                        "Dados do Alimento",
                        color = CABranco, fontWeight = FontWeight.Bold, fontSize = 22.sp
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        "Preencha as informações sobre o alimento que será doado.",
                        color = CABranco.copy(alpha = 0.85f), fontSize = 13.sp,
                        lineHeight = 18.sp
                    )
                }
            }


            Column(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {

                CampoFormulario(
                    label = "Nome do alimento *",
                    icone = Icons.Default.Fastfood,
                    placeholder = "Ex.: Cesta básica, Marmita, Pão..."
                ) {
                    CampoTexto(
                        value = viewModel.nome,
                        onValueChange = { if (it.length <= 50) viewModel.nome = it },
                        placeholder = "Ex.: Cesta básica",
                        contador = "${viewModel.nome.length}/50",
                        readOnly = false
                    )
                }


                CampoFormulario(
                    label = "Descrição *",
                    icone = Icons.Default.Description,
                    placeholder = "Descreva o alimento brevemente"
                ) {
                    CampoTexto(
                        value = viewModel.descricao,
                        onValueChange = { if (it.length <= 100) viewModel.descricao = it },
                        placeholder = "Ex.: Arroz, feijão, macarrão e óleo",
                        maxLines = 3,
                        contador = "${viewModel.descricao.length}/100"
                    )
                }


                CampoFormulario(
                    label = "Quantidade *",
                    icone = Icons.Default.Inventory2,
                    placeholder = "Número de unidades/porções"
                ) {
                    CampoTexto(
                        value = viewModel.quantidade,
                        onValueChange = {
                            if (it.all { c -> c.isDigit() }) viewModel.quantidade = it
                        },
                        placeholder = "Ex.: 5",
                        keyboardType = KeyboardType.Number
                    )
                }


                CampoFormulario(
                    label = "Data de validade *",
                    icone = Icons.Default.EventBusy,
                    placeholder = "Selecione a data"
                ) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        CampoTexto(
                            value = viewModel.validade,
                            onValueChange = { },
                            placeholder = "Clique para selecionar",
                            readOnly = true
                        )
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .clickable { mostrarCalendario = true }
                        )
                    }
                }


                CampoFormulario(
                    label = "Categoria *",
                    icone = Icons.Default.Category,
                    placeholder = "Selecione a categoria"
                ) {
                    ExposedDropdownMenuBox(
                        expanded = categoriaExpandida,
                        onExpandedChange = { categoriaExpandida = it }
                    ) {
                        OutlinedTextField(
                            value = viewModel.categoriaSelecionada?.nome ?: "",
                            onValueChange = {},
                            readOnly = true,
                            placeholder = {
                                Text(
                                    "Selecione...",
                                    color = Color(0xFFBBBBBB), fontSize = 14.sp
                                )
                            },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(
                                    expanded = categoriaExpandida
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = CAVermelho,
                                unfocusedBorderColor = CABorda,
                                focusedContainerColor = CABranco,
                                unfocusedContainerColor = CABranco,
                                cursorColor = CAVermelho
                            )
                        )
                        ExposedDropdownMenu(
                            expanded = categoriaExpandida,
                            onDismissRequest = { categoriaExpandida = false }
                        ) {
                            if (viewModel.carregandoCategorias) {
                                DropdownMenuItem(
                                    text = { Text("Buscando no servidor...", color = CASub, fontSize = 13.sp) },
                                    onClick = {}
                                )
                            }
                            else if (viewModel.listaCategorias.isEmpty()) {
                                DropdownMenuItem(
                                    text = { Text("Nenhuma categoria encontrada", color = CASub, fontSize = 13.sp) },
                                    onClick = {}
                                )
                            }
                            else {
                                viewModel.listaCategorias.forEach { cat ->
                                    DropdownMenuItem(
                                        text = { Text(cat.nome, fontSize = 14.sp) },
                                        onClick = {
                                            viewModel.categoriaSelecionada = cat
                                            categoriaExpandida = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(Modifier.height(8.dp))

                if (viewModel.mensagemErro.isNotBlank()) {
                    Text(
                        text = viewModel.mensagemErro,
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                Button(
                    onClick = {
                        if (camposValidos) {
                            onProximo(
                                AlimentoFormData(
                                    idAlimento = 0,
                                    nome = viewModel.nome,
                                    descricao = viewModel.descricao,
                                    quantidade = viewModel.quantidade.toIntOrNull() ?: 0,
                                    validade = viewModel.validade,
                                    idCategoria = viewModel.categoriaSelecionada?.id ?: 0

                                )
                            )
                        }
                    },
                    enabled = camposValidos,
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CAVermelho,
                        contentColor = CABranco,
                        disabledContainerColor = CAVermelho.copy(alpha = 0.35f),
                        disabledContentColor = CABranco.copy(alpha = 0.5f)
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 3.dp)
                ) {
                    Text("Próximo", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(Modifier.width(8.dp))
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowForward, null,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(Modifier.height(16.dp))
            }

            if (mostrarCalendario) {
                DatePickerDialog(
                    onDismissRequest = { mostrarCalendario = false },
                    confirmButton = {
                        TextButton(onClick = {
                            datePickerState.selectedDateMillis?.let { milis ->
                                viewModel.validade = FormatarData(milis)
                            }
                            mostrarCalendario = false
                        }) {
                            Text("OK")
                        }
                    }
                ) {
                    DatePicker(state = datePickerState)
                }
            }

        }
    }
}



@Composable
fun IndicadorPassos(passoAtual: Int, totalPassos: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(CABranco)
            .padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(totalPassos) { index ->
            val ativo = index + 1 <= passoAtual
            val largura = if (ativo) 40.dp else 24.dp
            Box(
                modifier = Modifier
                    .height(4.dp)
                    .width(largura)
                    .clip(RoundedCornerShape(2.dp))
                    .background(if (ativo) CAVermelho else Color(0xFFDDDDDD))
            )
        }
        Spacer(Modifier.width(4.dp))
        Text("$passoAtual de $totalPassos",
            fontSize = 11.sp, color = CASub)
    }
}

@Composable
fun CampoFormulario(
    label: String,
    icone: ImageVector,
    placeholder: String,
    content: @Composable () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icone, null, tint = CAVermelho, modifier = Modifier.size(16.dp))
            Spacer(Modifier.width(6.dp))
            Text(label, fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold, color = CATexto)
        }
        content()
    }
}

@Composable
fun CampoTexto(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    maxLines: Int = 1,
    contador: String? = null,
    readOnly: Boolean = false
) {
    Column {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            readOnly = readOnly,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(placeholder, color = Color(0xFFBBBBBB), fontSize = 14.sp)
            },
            singleLine = maxLines == 1,
            maxLines = maxLines,
            shape = RoundedCornerShape(10.dp),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = CAVermelho,
                unfocusedBorderColor = CABorda,
                focusedContainerColor = CABranco,
                unfocusedContainerColor = CABranco,
                cursorColor = CAVermelho
            )
        )

        if (contador != null) {
            Text(
                contador,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.End),
                fontSize = 10.sp,
                color = CASub
            )
        }
    }
}
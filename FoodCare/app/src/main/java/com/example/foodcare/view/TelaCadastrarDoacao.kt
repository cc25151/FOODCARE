package com.example.foodcare.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foodcare.ui.theme.*



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaCadastrarDoacao(
    alimentoFormData: AlimentoFormData    = AlimentoFormData("", "", 0, "", 0),
    onVoltar: () -> Unit                  = {},
    onConfirmar: (DoacaoFormData) -> Unit = {}
) {
    var dataDoacao      by remember { mutableStateOf("") }
    var horarioInicial  by remember { mutableStateOf("") }
    var horarioFinal    by remember { mutableStateOf("") }

    var confirmacaoVisivel by remember { mutableStateOf(false) }

    val camposValidos = dataDoacao.isNotBlank()
            && horarioInicial.isNotBlank()
            && horarioFinal.isNotBlank()

    Scaffold(
        containerColor = CDFundo,
        topBar = {
            TopAppBar(
                title = {
                    Text("Nova Doação", fontWeight = FontWeight.Bold,
                        fontSize = 18.sp, color = CDBranco)
                },
                navigationIcon = {
                    IconButton(onClick = onVoltar) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack,
                            "Voltar", tint = CDBranco)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = CDVermelho)
            )
        }
    ) { pad ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(pad)
                .verticalScroll(rememberScrollState())
        ) {

            IndicadorPassos(passoAtual = 2, totalPassos = 2)

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(listOf(CDVermelhoC, CDVermelhoE))
                    )
                    .padding(horizontal = 20.dp, vertical = 20.dp)
            ) {
                Column {
                    Text("Passo 2 de 2", color = CDBranco.copy(alpha = 0.75f),
                        fontSize = 12.sp)
                    Text("Dados da Doação",
                        color = CDBranco, fontWeight = FontWeight.Bold, fontSize = 22.sp)
                    Spacer(Modifier.height(2.dp))
                    Text("Informe quando e em qual janela de horário a doação estará disponível.",
                        color = CDBranco.copy(alpha = 0.85f), fontSize = 13.sp,
                        lineHeight = 18.sp)
                }
            }

            Column(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {


                Card(
                    shape  = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = CDBranco),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(CDVermelho.copy(alpha = 0.08f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Fastfood, null,
                                tint = CDVermelho, modifier = Modifier.size(22.dp))
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Alimento selecionado",
                                fontSize = 10.sp, color = CDSub)
                            Text(
                                alimentoFormData.nome.ifBlank { "—" },
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = CDTexto
                            )
                            Text(
                                "Qtd: ${alimentoFormData.quantidade}  ·  Validade: ${alimentoFormData.validade}",
                                fontSize = 11.sp, color = CDSub
                            )
                        }
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = CDVerdeBg
                        ) {
                            Icon(Icons.Default.Check, null,
                                tint = CDVerde,
                                modifier = Modifier
                                    .padding(6.dp)
                                    .size(14.dp))
                        }
                    }
                }

                HorizontalDivider(color = Color(0xFFEEEEEE))


                CampoFormulario(
                    label       = "Data da doação *",
                    icone       = Icons.Default.Today,
                    placeholder = "DD/MM/AAAA"
                ) {
                    CampoTexto(
                        value         = dataDoacao,
                        onValueChange = { dataDoacao = it },
                        placeholder   = "DD/MM/AAAA",
                        keyboardType  = KeyboardType.Number
                    )
                }


                CampoFormulario(
                    label       = "Horário de início *",
                    icone       = Icons.Default.Schedule,
                    placeholder = "HH:MM"
                ) {
                    CampoTexto(
                        value         = horarioInicial,
                        onValueChange = { horarioInicial = it },
                        placeholder   = "Ex.: 08:00",
                        keyboardType  = KeyboardType.Number
                    )
                }


                CampoFormulario(
                    label       = "Horário de encerramento *",
                    icone       = Icons.Default.HourglassBottom,
                    placeholder = "HH:MM"
                ) {
                    CampoTexto(
                        value         = horarioFinal,
                        onValueChange = { horarioFinal = it },
                        placeholder   = "Ex.: 12:00",
                        keyboardType  = KeyboardType.Number
                    )
                }


                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFFF3E5F5),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(Icons.Default.Info, null,
                            tint = Color(0xFF6A1B9A),
                            modifier = Modifier.size(16.dp).padding(top = 1.dp))
                        Text(
                            "O receptor e a avaliação serão preenchidos " +
                            "automaticamente após a conclusão da doação.",
                            fontSize = 12.sp,
                            color = Color(0xFF4A148C),
                            lineHeight = 17.sp
                        )
                    }
                }

                Spacer(Modifier.height(4.dp))


                Button(
                    onClick = {
                        if (camposValidos) confirmacaoVisivel = true
                    },
                    enabled  = camposValidos,
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape    = RoundedCornerShape(14.dp),
                    colors   = ButtonDefaults.buttonColors(
                        containerColor         = CDVermelho,
                        contentColor           = CDBranco,
                        disabledContainerColor = CDVermelho.copy(alpha = 0.35f),
                        disabledContentColor   = CDBranco.copy(alpha = 0.5f)
                    ),
                    elevation = ButtonDefaults.buttonElevation(3.dp)
                ) {
                    Icon(Icons.Default.Check, null, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Confirmar doação", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }

                Spacer(Modifier.height(16.dp))
            }
        }
    }


    if (confirmacaoVisivel) {
        AlertDialog(
            onDismissRequest = { confirmacaoVisivel = false },
            icon = {
                Icon(Icons.Default.VolunteerActivism, null,
                    tint = CDVermelho, modifier = Modifier.size(32.dp))
            },
            title = {
                Text("Confirmar doação?",
                    fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
            },
            text = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text("Alimento: ${alimentoFormData.nome.ifBlank { "—" }}",
                        fontSize = 13.sp, color = CDSub)
                    Text("Data: $dataDoacao",
                        fontSize = 13.sp, color = CDSub)
                    Text("Horário: $horarioInicial – $horarioFinal",
                        fontSize = 13.sp, color = CDSub)
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        confirmacaoVisivel = false
                        onConfirmar(
                            DoacaoFormData(
                                dataDoacao     = dataDoacao,
                                horarioInicial = horarioInicial,
                                horarioFinal   = horarioFinal
                            )
                        )
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CDVermelho),
                    shape  = RoundedCornerShape(10.dp)
                ) {
                    Text("Confirmar", color = CDBranco, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { confirmacaoVisivel = false }) {
                    Text("Cancelar", color = CDSub)
                }
            },
            shape = RoundedCornerShape(16.dp)
        )
    }
}


data class DoacaoFormData(
    val dataDoacao: String,
    val horarioInicial: String,
    val horarioFinal: String
)
